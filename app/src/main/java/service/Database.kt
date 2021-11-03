package service

import android.util.Log
import android.widget.Toast
import com.example.fundo.home.Adapter
import com.example.fundo.home.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import model.UserDetails

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
        var key = first+second
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("user")
            .child(uid).child("Notes").child(key)
            .setValue(notes).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "note successfully added")
                    Log.d("test",key)
                }
            }
    }




     fun updateNote(key: String, title: String, content: String) {
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var database = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Notes")
        val note = mapOf<String,String>(
            "title" to title,
            "content" to content
        )
        database.child(key).updateChildren(note).addOnSuccessListener {
           Log.d("test","updated")
        }.addOnFailureListener {
           Log.d("test","not updated")
        }
    }



     fun getNotesData(noteArrayList:ArrayList<Notes>):ArrayList<Notes> {

        lateinit var dbref: DatabaseReference
        dbref = FirebaseDatabase.getInstance().getReference("Notes")
        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {
                    for(noteSnapshot in snapshot.children) {
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

}