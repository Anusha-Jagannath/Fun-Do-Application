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

class ReminderNotes : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var reminderRecyclerView: RecyclerView
    private lateinit var reminderArrayList: ArrayList<Notes>
    private lateinit var back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_notes)
        back = findViewById(R.id.moveBack)
        reminderRecyclerView = findViewById(R.id.reminderList)
        reminderRecyclerView.layoutManager = LinearLayoutManager(this)
        reminderRecyclerView.setHasFixedSize(true)
        reminderArrayList = arrayListOf<Notes>()
        back.setOnClickListener {
            gotoHomePage()
        }
        getReminderList()
    }

    private fun getReminderList() {
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("ReminderNotes").child(uid)
        dbref.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(reminderSnapshot in snapshot.children) {
                        val reminder = reminderSnapshot.getValue(Notes::class.java)
                        reminderArrayList.add(reminder!!)

                    }
                    var adapter = NotesAdapter(reminderArrayList)
                    reminderRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object: NotesAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            Toast.makeText(this@ReminderNotes,"clicked on position $position",Toast.LENGTH_SHORT).show()
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