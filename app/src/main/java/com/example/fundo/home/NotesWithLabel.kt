package com.example.fundo.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import com.example.fundo.R
import com.example.fundo.service.Database
import com.example.fundo.service.DatabaseService
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesWithLabel : AppCompatActivity() {
    private lateinit var back: ImageView
    private lateinit var work: CheckBox
    private lateinit var music: CheckBox
    private lateinit var general: CheckBox
    private lateinit var saveButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_with_label)
        back = findViewById(R.id.imageback)
        work = findViewById(R.id.checkBoxWork)
        music = findViewById(R.id.checkBoxMusic)
        general = findViewById(R.id.checkBoxGeneral)
        saveButton = findViewById(R.id.labelSave)
        var title = intent.getStringExtra("title")
        var content = intent.getStringExtra("content")
        Toast.makeText(this,"$title $content",Toast.LENGTH_SHORT).show()

        back.setOnClickListener {
            gotoAddNotesPage()
        }

        saveButton.setOnClickListener {
            Toast.makeText(this,"label clicked",Toast.LENGTH_SHORT).show()
            var checkboxInput = ""
            if(work.isChecked) {
                checkboxInput += "work"
            }
            if(music.isChecked) {
                checkboxInput += " music"
            }
            if(general.isChecked) {
                checkboxInput += " general"
            }
            Toast.makeText(this,"$checkboxInput",Toast.LENGTH_SHORT).show()
            var note = Notes(title,content, null,null,null,null,checkboxInput)
            var database = Database()
            database.saveNotesWithLabel(note)
            Toast.makeText(this,"label added successfully",Toast.LENGTH_SHORT).show()

            var key = title+content
            var databaseService = DatabaseService(this)
            databaseService.linkNoteWithLabel(key,checkboxInput)
        }
    }

    private fun gotoAddNotesPage() {
        var intent = Intent(this,AddNotesActivity::class.java)
        startActivity(intent)
    }
}