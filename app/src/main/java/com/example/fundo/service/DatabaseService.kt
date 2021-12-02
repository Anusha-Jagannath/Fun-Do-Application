package com.example.fundo.service

import android.content.Context
import android.util.Log
import com.example.fundo.home.Notes
import com.example.fundo.util.NetworkHandler
import com.example.fundo.room.note.NoteDatabase
import com.example.fundo.room.note.NoteKey
import com.example.fundo.room.note.NotesEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class DatabaseService(context: Context) {

    private val noteDatabase = NoteDatabase.getInstance(context)
    private val noteDAO = noteDatabase.noteDao()
    private val networkStatus = NetworkHandler.checkForInternet(context)
    private val fid = AuthenticationService().getUid()


    fun addNotesToDB(title: String, content: String) {
        val cal = Calendar.getInstance()
        var date = cal.time.toString()
        if (networkStatus) {
            Log.d("Network check", "network present")
            val database = Database()
            var notes = Notes(title, content, date, null, null)
            database.saveNotes(notes)

            val noteInfo = NoteKey(null, fid, title, content, date)
            GlobalScope.launch(Dispatchers.IO) {
                noteDAO.insert(noteInfo)
            }
        } else {
            val noteInfo = NoteKey(null, null, title, content, date)
            GlobalScope.launch(Dispatchers.IO) {
                noteDAO.insert(noteInfo)
            }
            syncForInsert()
        }
    }

    private fun syncForInsert() {
        var localList: List<NoteKey>
        if (networkStatus) {
            GlobalScope.launch(Dispatchers.IO) {
                localList = noteDAO.display()
                for (list in localList) {
                    if (list.fid == null) {
                        val database = Database()
                        var notes = Notes(list.title, list.content, list.date)
                        database.saveNotes(notes)
                    }
                }
            }

        }
    }

    fun updateNotesToDB(key: String, newTitle: String, newContent: String, context: Context) {

        val network = NetworkHandler.checkForInternet(context)
        val cal = Calendar.getInstance()
        var date = cal.time.toString()
        if (network) {
            var database = Database()
            database.updateNote(key, newTitle, newContent)
            val noteInfo = NoteKey(null, fid, newTitle, newContent, date)
            GlobalScope.launch(Dispatchers.IO) {
                noteDatabase.noteDao().update(noteInfo)
            }
        } else {
            val noteInfo = NoteKey(null, fid, newTitle, newContent, date)
            GlobalScope.launch(Dispatchers.IO) {
                noteDatabase.noteDao().update(noteInfo)
            }
            syncForUpdate(key)

        }
    }

    private fun syncForUpdate(key: String) {
        var remoteList = Database().getNotesData()
        val database = Database()
        var localList: List<NoteKey>
        if (networkStatus) {
            GlobalScope.launch(Dispatchers.IO) {
                localList = noteDAO.display()
                for (i in 0..localList.size - 1) {
                    if (remoteList[i].title?.contains(localList[i].title.toString()) == true) {
                        if (localList.get(i).title == remoteList.get(i).title &&
                            localList.get(i).date?.toInt()!! > remoteList.get(i).date?.toInt()!!
                        ) {
                            database.updateNote(
                                key,
                                localList.get(i).title!!,
                                localList.get(i).date!!
                            )
                        }
                    } else {
                        val key = remoteList[i].title.toString() + remoteList[i].content
                        database.deleteNote(key)
                    }

                }
            }

        }
    }

    fun deleteNotesFromDB(key: String, title: String, context: Context) {
        val network = NetworkHandler.checkForInternet(context)
        if (network) {
            var database = Database()
            database.deleteNote(key)
            GlobalScope.launch(Dispatchers.IO) {
                noteDAO.delete(title)
            }
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                noteDAO.delete(title)
            }
        }
    }

    fun addDataToDB(title: String, content: String, helper: DatabaseHelper) {
        var sqliteDatabaseService = SQLiteDatabaseService()
        sqliteDatabaseService.addDataToDB(title, content, helper)
    }

    fun deleteDataFromDB(title: String, helper: DatabaseHelper) {
        var sqliteDatabaseService = SQLiteDatabaseService()
        sqliteDatabaseService.deleteDataFromDB(title, helper)
    }


    fun updateDataToDB(
        title: String,
        newtitle: String,
        newcontent: String,
        helper: DatabaseHelper
    ) {
        var sqliteDatabaseService = SQLiteDatabaseService()
        sqliteDatabaseService.updateDataToDB(title, newtitle, newcontent, helper)
    }

    fun deletePermanent(key: String) {
        var database = Database()
        database.deletePermanent(key)
    }

    fun addLabelToDB(inputLabel: String) {
        var database = Database()
        database.addLabelToDB(inputLabel)
    }

    fun deleteLabel(key: String) {
        var database = Database()
        database.deleteLabelFromDB(key)
    }

    fun updateLabel(inputLabel: String, updatedLabel: String) {
        var database = Database()
        database.updateLabel(inputLabel, updatedLabel)
    }

    fun loadPage(key: String, offset: Int, title: String, content: String) {
        val cal = Calendar.getInstance()
        var date = cal.time.toString()
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        val dbref = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Notes")
        getData(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    CoroutineScope(Dispatchers.Default).launch {
                        for (noteSnapshot in snapshot.children) {
                            val note = noteSnapshot.getValue(Notes::class.java)
                            val noteInfo =
                                NoteKey(null, fid, note?.title.toString(), note?.content, date)
                            CoroutineScope(Dispatchers.IO).launch {
                                noteDAO.insert(noteInfo)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        noteDAO.getPagedNotes(10, offset)
    }


    private fun getData(key: String): Query {
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

    fun linkNoteWithLabel(key: String, labelName: String) {
        var database = Database()
        database.linkNoteWithLabel(key, labelName)
    }
}


