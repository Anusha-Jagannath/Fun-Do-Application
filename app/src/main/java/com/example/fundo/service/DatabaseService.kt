package com.example.fundo.service

import android.content.Context
import android.util.Log
import com.example.fundo.home.Notes
import com.example.fundo.util.NetworkHandler
import com.example.fundo.room.note.NoteDatabase
import com.example.fundo.room.note.NotesEntity
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
            var notes = Notes(title, content, date)
            database.saveNotes(notes)

            val noteInfo = NotesEntity(null, fid, title, content, date)
            GlobalScope.launch(Dispatchers.IO) {
                noteDAO.insert(noteInfo)
            }
        } else {
            val noteInfo = NotesEntity(null, null, title, content, date)
            GlobalScope.launch(Dispatchers.IO) {
                noteDAO.insert(noteInfo)
            }
            syncForInsert()
        }
    }

    private fun syncForInsert() {
        var localList: List<NotesEntity>
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
            val noteInfo = NotesEntity(null, fid, newTitle, newContent, date)
            GlobalScope.launch(Dispatchers.IO) {
                noteDatabase.noteDao().update(noteInfo)
            }
        } else {
            val noteInfo = NotesEntity(null, fid, newTitle, newContent, date)
            GlobalScope.launch(Dispatchers.IO) {
                noteDatabase.noteDao().update(noteInfo)
            }
            syncForUpdate(key)

        }
    }

    private fun syncForUpdate(key: String) {
        var remoteList = Database().getNotesData()
        val database = Database()
        var localList: List<NotesEntity>
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

}


