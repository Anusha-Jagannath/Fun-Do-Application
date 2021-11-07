package com.example.fundo.home

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.fundo.R
import com.example.fundo.ui.HomeActivityNew
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import service.Database
import service.DatabaseHelper
import service.DatabaseService

class AddNotesActivity : AppCompatActivity() {

    lateinit var saveNotesButton: FloatingActionButton
    lateinit var backButton: ImageView
    lateinit var addtitle: EditText
    lateinit var addContent: EditText
    lateinit var updateNoteButton: Button
    lateinit var deleteNoteButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        addtitle = findViewById(R.id.inputTitle)
        addContent = findViewById(R.id.inputNote)

        saveNotesButton = findViewById(R.id.saveNoteBtn)
        backButton = findViewById(R.id.backButton)
        updateNoteButton = findViewById(R.id.updateBtn)
        deleteNoteButton = findViewById(R.id.deleteImage)

        var title = intent.getStringExtra("title")
        var content = intent.getStringExtra("content")

        addtitle.setText(title)
        addContent.setText(content)

        var helper = DatabaseHelper(applicationContext)



        saveNotesButton.setOnClickListener {
            Toast.makeText(this, "save notes button clicked", Toast.LENGTH_SHORT).show()
            var title = addtitle.text.toString()
            var note = addContent.text.toString()

            if (title.isNotEmpty() || note.isNotEmpty()) {
                val database = Database()
                var notes = Notes(title, note)
                database.saveNotes(notes)
                Toast.makeText(applicationContext, "Note saved", Toast.LENGTH_SHORT).show()

                var databaseService = DatabaseService()
                databaseService.addDataToDB(title,note,helper)
                Toast.makeText(this,"note saved to sqlite",Toast.LENGTH_SHORT).show()


            } else {
                Toast.makeText(applicationContext, "Empty note discarded", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        backButton.setOnClickListener {
            Toast.makeText(this, "back button clicked", Toast.LENGTH_SHORT).show()
            gotoHomePage()
        }

        updateNoteButton.setOnClickListener {
            Log.d("update note", "update button clicked")
            Toast.makeText(this, "update clicked", Toast.LENGTH_SHORT).show()
            var key = title + content
            Toast.makeText(this, "$key", Toast.LENGTH_SHORT).show()
            var newTitle = addtitle.text.toString()
            var newContent = addContent.text.toString()
            var database = Database()
            database.updateNote(key, newTitle, newContent)
            var databaseService = DatabaseService()
            if (title != null) {
                databaseService.updateDataToDB(title,newTitle,newContent,helper)
            }

        }

        deleteNoteButton.setOnClickListener {
            var key = title + content
            Toast.makeText(this,"delete button clicked",Toast.LENGTH_SHORT).show()
            var database = Database()
            database.deleteNote(key)

            var databaseService = DatabaseService()
            if (title != null) {
                databaseService.deleteDataFromDB(title,helper)
            }
        }



    }


    private fun gotoHomePage() {
        var intent = Intent(this, HomeActivityNew::class.java)
        startActivity(intent)

    }
}