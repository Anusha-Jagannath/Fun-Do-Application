package com.example.fundo.home

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.ui.HomeActivityNew
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.fundo.service.Database

class DeletedNotes : AppCompatActivity() {

    private lateinit var deletedRecyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<Notes>
    lateinit var backButton: ImageView
    lateinit var adapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deleted_notes)
        deletedRecyclerView = findViewById(R.id.deletedList)
        deletedRecyclerView.layoutManager = LinearLayoutManager(this)
        deletedRecyclerView.setHasFixedSize(true)
        noteArrayList = arrayListOf<Notes>()
        adapter = NotesAdapter(noteArrayList)
        deletedRecyclerView.adapter = adapter

        backButton = findViewById(R.id.backImageView)
        backButton.setOnClickListener {
            gotoHomeActivity()
        }
        getDeletedNotes()
    }

    private fun gotoHomeActivity() {
        var intent = Intent(this, HomeActivityNew::class.java)
        startActivity(intent)
    }

    private fun getDeletedNotes() {
        lateinit var dbref: DatabaseReference
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref =
            FirebaseDatabase.getInstance().getReference("deletedNotes").child(uid).child("Notes")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (noteSnapshot in snapshot.children) {
                        val notes = noteSnapshot.getValue(Notes::class.java)
                        noteArrayList.add(notes!!)
                    }
                    adapter.setOnItemClickListener(object : NotesAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            Toast.makeText(
                                applicationContext,
                                "clicked on $position",
                                Toast.LENGTH_SHORT
                            ).show()
                            var note = noteArrayList[position]
                            openAlert(note,position)
                        }

                    })

                }

                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun openAlert(notes: Notes,position: Int) {
        var key = notes.title + notes.content
        if (key != null) {
            Log.d("Key", key)
        }
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("Do you want to delete permanently")
        alertDialog.setTitle("Archive note")
        alertDialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            var database = Database()
            if (key != null) {
                database.deletePermanent(key)
            }
            noteArrayList.remove(notes)
            adapter.notifyItemChanged(position)
        })
        alertDialog.setNegativeButton(
            "Restore",
            DialogInterface.OnClickListener { dialogInterface, i ->
                var database = Database()
                database.saveNotes(notes)
                if (key != null) {
                    database.deletePermanent(key)
                }
                database.saveArchivedNotes(notes)
            })
        alertDialog.show()
    }
}