package com.example.fundo.service

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class SQLiteDatabaseService {

    fun addDataToDB(title: String, content: String, helper: DatabaseHelper) {
        var db = helper.readableDatabase
        var cv = ContentValues()
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        cv.put("UID",uid)
        cv.put("TITLE",title)
        cv.put("CONTENT",content)
        var result = db.insert("NOTES",null,cv)
        if(result.toInt() == -1) {
            Log.d("Addnotes","notes not added to sqlite")
        }
        else {
            Log.d("Addnotes","notes added to sqlite")
        }
    }


    fun deleteDataFromDB(title: String,helper: DatabaseHelper) {
        var db = helper.readableDatabase
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