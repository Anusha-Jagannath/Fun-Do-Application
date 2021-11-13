package com.example.fundo.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.fundo.R
import com.example.fundo.ui.HomeActivityNew

class DeletedNotes : AppCompatActivity() {
    lateinit var backButton:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deleted_notes)
        backButton = findViewById(R.id.backImageView)
        backButton.setOnClickListener {
            gotoHomeActivity()
        }
    }

    private fun gotoHomeActivity() {
        var intent = Intent(this,HomeActivityNew::class.java)
        startActivity(intent)
    }
}