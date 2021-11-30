package com.example.fundo.label

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.home.Notes
import com.example.fundo.home.NotesAdapter
import com.example.fundo.ui.HomeActivityNew
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ArchivedNotes : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var dbref : DatabaseReference
    private lateinit var archiveRecyclerView: RecyclerView
    private lateinit var archiveArrayList: ArrayList<Notes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archived_notes)
        backButton = findViewById(R.id.back)
        archiveRecyclerView = findViewById(R.id.archiveList)
        archiveRecyclerView.layoutManager = LinearLayoutManager(this)
        archiveRecyclerView.setHasFixedSize(true)
        archiveArrayList = arrayListOf<Notes>()
        backButton.setOnClickListener {
            Toast.makeText(this,"back",Toast.LENGTH_SHORT).show()
            gotoHomePage()
        }
        getArchiveNotes()
    }

    private fun getArchiveNotes() {

        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("ArchivedNotes").child(uid)
        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.exists()) {
                   for(archiveSnapshot in snapshot.children) {
                       val archive = archiveSnapshot.getValue(Notes::class.java)
                       archiveArrayList.add(archive!!)
                   }
                   var adapter = NotesAdapter(archiveArrayList)
                   archiveRecyclerView.adapter = adapter
                   adapter.setOnItemClickListener(object: NotesAdapter.onItemClickListener{
                       override fun onItemClick(position: Int) {
                           Toast.makeText(this@ArchivedNotes,"clicked on $position",Toast.LENGTH_SHORT).show()
                       }

                   })

               }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun gotoHomePage() {
       var intent = Intent(this,HomeActivityNew::class.java)
        startActivity(intent)

    }
}