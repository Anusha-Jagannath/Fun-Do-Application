package com.example.fundo.service

import android.util.Log
import com.example.fundo.home.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.fundo.model.UserDetails
import java.util.*
import kotlin.collections.ArrayList

class Database {

    fun saveUserData(user: UserDetails) {
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "successfully added")
                }
            }
    }


    fun getData(): String {
        var fullName = ""
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {

                if (it.exists()) {
                    fullName = it.child("userName").value.toString()
                }
            }
        return fullName
    }


    fun saveNotes(notes: Notes) {
        var first = notes.title
        var second = notes.content
        var key = first + second
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("user")
            .child(uid).child("Notes").child(key)
            .setValue(notes).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "note successfully added")
                    Log.d("test", key)
                }
            }
    }


    fun updateNote(key: String, title: String, content: String) {
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        val cal = Calendar.getInstance()
        var date = cal.time.toString()
        var database = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Notes")
        val note = mapOf<String, String>(
            "title" to title,
            "content" to content,
            "date" to date
        )
        database.child(key).updateChildren(note).addOnSuccessListener {
            Log.d("test", "updated")
        }.addOnFailureListener {
            Log.d("test", "not updated")
        }
    }


    fun getNotesData(): ArrayList<Notes> {
        lateinit var noteArrayList: ArrayList<Notes>
        lateinit var dbref: DatabaseReference
        dbref = FirebaseDatabase.getInstance().getReference("Notes")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (noteSnapshot in snapshot.children) {
                        val notes = noteSnapshot.getValue(Notes::class.java)
                        noteArrayList.add(notes!!)
                    }

                    //noteRecyclerView.adapter = Adapter(noteArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return noteArrayList
    }

    fun deleteNote(key: String) {
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var database = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Notes")
        database.child(key).removeValue().addOnSuccessListener {
            Log.d("test", "deleted")
        }.addOnFailureListener {
            Log.d("test", "not deleted")
        }
    }


    fun addDeletedNotesToDB(notes: Notes) {
        var first = notes.title
        var second = notes.content
        var key = first + second
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("deletedNotes")
            .child(uid).child("Notes").child(key)
            .setValue(notes).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "deleted note successfully added")
                    Log.d("test", key)
                }
            }
    }

    fun deletePermanent(key: String) {
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var database =
            FirebaseDatabase.getInstance().getReference("deletedNotes").child(uid).child("Notes")
        database.child(key).removeValue().addOnSuccessListener {
            Log.d("delete", "deleted permanent")
        }.addOnFailureListener {
            Log.d("test", "not deleted permanent")
        }
    }

}