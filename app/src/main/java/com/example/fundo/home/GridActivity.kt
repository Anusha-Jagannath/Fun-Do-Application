package com.example.fundo.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.ui.HomeActivityNew
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GridActivity : AppCompatActivity() {
    lateinit var backImage: ImageView

    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<Notes>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        backImage = findViewById(R.id.backImage)
        backImage.setOnClickListener {
            Log.d("GridActivity", "back button clicked")
            var intent = Intent(this, HomeActivityNew::class.java)
            startActivity(intent)
        }


        noteRecyclerView = findViewById(R.id.noteList)
        noteRecyclerView.layoutManager = GridLayoutManager(this, 2)
        noteRecyclerView.setHasFixedSize(true)
        noteArrayList = arrayListOf<Notes>()
        getNotesData()
    }


    private fun getNotesData() {

        lateinit var dbref: DatabaseReference
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Notes")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (noteSnapshot in snapshot.children) {
                        val notes = noteSnapshot.getValue(Notes::class.java)
                        noteArrayList.add(notes!!)
                    }


                    var adapter = Adapter(noteArrayList)
                    noteRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object: Adapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            Toast.makeText(applicationContext,"clicked on $position",Toast.LENGTH_SHORT).show()
                        }

                    })

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


}