package service

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class DatabaseService {

    fun addDataToDB(title: String, content: String, helper: DatabaseHelper) {
        var db = helper.readableDatabase
        var cv = ContentValues()
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        cv.put("UID",uid)
        cv.put("TITLE",title)
        cv.put("CONTENT",content)
        db.insert("NOTES",null,cv)
        Log.d("Addnotes","notes added to sqlite")
    }

    fun deleteDataFromDB(title: String,helper: DatabaseHelper) {
        var db = helper.readableDatabase
        var cv = ContentValues()
        var rs = db.rawQuery("SELECT * FROM NOTES",null)
        db.delete("NOTES","TITLE = ?", arrayOf(title))
        Log.d("test","deleted from sqlite")
    }

    fun updateDataToDB(title: String,newtitle: String, newcontent: String, helper: DatabaseHelper) {
        var db = helper.readableDatabase
        var cv = ContentValues()
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        cv.put("UID",uid)
        cv.put("TITLE",newtitle)
        cv.put("CONTENT",newcontent)
        db.update("NOTES",cv,"TITLE = ?", arrayOf(title))
        Log.d("update notes","notes updated to sqlite")
    }



}