package service

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.fundo.home.Notes
import com.example.fundo.network.NetworkHandler
import com.example.fundo.roomdata.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class DatabaseService(context: Context){

    private val noteDatabase = NoteDatabase.getInstance(context)
    private val noteDAO = noteDatabase.noteDao()
    private val networkStatus = NetworkHandler.checkForInternet(context)
    private val fid = AuthenticationService().getUid()


    fun addNotesToDB(title: String,content: String) {

        if (networkStatus) {
            Log.d("Network check","network present")
            val database = Database()
            val cal = Calendar.getInstance()
            var date = cal.time.toString()
            var notes = Notes(title, content, date)
            database.saveNotes(notes)

            val noteInfo = NotesEntity(null, fid, title, content, date)
            GlobalScope.launch(Dispatchers.IO) {
                noteDAO.insert(noteInfo)
            }
        }
    }

    fun updateNotesToDB(key: String,newTitle: String,newContent: String) {
        var database = Database()
        database.updateNote(key, newTitle, newContent)
        val cal = Calendar.getInstance()
        var date = cal.time.toString()
        val noteInfo = NotesEntity(null,fid, newTitle, newContent,date)
        GlobalScope.launch(Dispatchers.IO) {
            noteDAO.update(noteInfo)
        }
    }

    fun deleteNotesFromDB(key: String,title: String) {
        var database = Database()
        database.deleteNote(key)
        GlobalScope.launch(Dispatchers.IO){
                noteDAO.delete(title)
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

}