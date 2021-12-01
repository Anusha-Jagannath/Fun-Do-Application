package com.example.fundo.service

import android.util.Log
import com.example.fundo.home.Notes
import com.example.fundo.model.Labels
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

    fun get(key: String): Query {
        lateinit var dbref: DatabaseReference
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Notes")

        if (key == " ") {
            return dbref.orderByKey().limitToFirst(8)
        } else {
            //dbref.equalTo("randomlabel","labelId")
            return dbref.orderByKey().startAfter(key).limitToFirst(8)
        }

    }

    fun saveLabel(key: String, labelInput: String) {
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var labelKey = UUID.randomUUID().toString()
        var label = Labels(labelKey, labelInput)
        Log.d("KEY", labelKey)
        Log.d("Label", label.labelId.toString())

        FirebaseDatabase.getInstance().getReference("user")
            .child(uid).child("Notes").child(key).child(labelKey)
            .setValue(label).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("LABEL", "label successfully added")
                    Log.d("test", key)
                }
            }
    }

    fun saveLabelToNotesOne(key: String, notes: Notes, labelInput: String) {
        val cal = Calendar.getInstance()
        var date = cal.time.toString()
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var newNote = Notes(notes.title, notes.content, date, labelInput, null)
        FirebaseDatabase.getInstance().getReference("user")
            .child(uid).child("Notes").child(key)
            .setValue(newNote).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "label1 successfully added")
                    Log.d("test", key)
                }
            }

    }


    fun saveLabelToNotesSecond(key: String, notes: Notes, labelInput: String) {
        val cal = Calendar.getInstance()
        var date = cal.time.toString()
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var newNote = Notes(notes.title, notes.content, date, notes.label1, labelInput)
        FirebaseDatabase.getInstance().getReference("user")
            .child(uid).child("Notes").child(key)
            .setValue(newNote).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "label2 successfully added")
                    Log.d("test", key)
                }
            }
    }
    fun addLabelToDB(labelInput: String) {
        var key = UUID.randomUUID().toString()
        var label = Labels(key,labelInput)
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("Label")
            .child(uid).child(key)
            .setValue(label).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "label successfully added")
                    Log.d("test", key)
                }
            }
    }

    fun deleteLabelFromDB(key: String) {
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var database = FirebaseDatabase.getInstance().getReference("Label").child(uid)
        database.child(key).removeValue().addOnSuccessListener {
            Log.d("test", "label deleted")
        }.addOnFailureListener {
            Log.d("test", "not deleted")
        }
    }

    fun updateLabel(key: String, updatedLabel: String) {
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var database = FirebaseDatabase.getInstance().getReference("Label").child(uid)
        val label = mapOf<String, String>(
            "labelId" to key,
            "labelName" to updatedLabel
        )
        database.child(key).updateChildren(label).addOnSuccessListener {
            Log.d("test", "label updated")
        }.addOnFailureListener {
            Log.d("test", "not updated")
        }
    }

    fun saveArchivedNotes(notes: Notes) {
        var first = notes.title
        var second = notes.content
        var key = first + second
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("ArchivedNotes")
            .child(uid).child(key)
            .setValue(notes).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "note successfully added")
                    Log.d("test", key)
                }
            }
    }

    fun saveReminderNotes(notes: Notes) {
        var first = notes.title
        var second = notes.content
        var key = first + second
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("ReminderNotes")
            .child(uid).child(key)
            .setValue(notes).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "note successfully added")
                    Log.d("test", key)
                }
            }
    }

    fun saveNotesWithLabel(notes: Notes) {
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




}