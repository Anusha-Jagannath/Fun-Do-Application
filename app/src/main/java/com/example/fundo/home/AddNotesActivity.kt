package com.example.fundo.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.fundo.R
import com.example.fundo.ui.HomeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddNotesActivity : AppCompatActivity() {

    lateinit var saveNotesButton: FloatingActionButton
    lateinit var backButton: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        saveNotesButton = findViewById(R.id.saveNoteBtn)
        backButton = findViewById(R.id.backButton)
        saveNotesButton.setOnClickListener {
            Toast.makeText(this,"save notes button clicked",Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            Toast.makeText(this,"back button clicked",Toast.LENGTH_SHORT).show()
            gotoHomePage()
        }
    }

    private fun gotoHomePage() {
        var intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)

    }
}