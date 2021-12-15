package com.example.fundo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.fundo.networking.users.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestActivity : AppCompatActivity() {
    private lateinit var callApiButton: Button
    private lateinit var apiTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        callApiButton = findViewById(R.id.callApiButton)
        apiTextView = findViewById(R.id.responseText)
        val userService = UserService()
        callApiButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val users = userService.getUsers()
                withContext(Dispatchers.Main) {
                    apiTextView.text = users.toString()
                }
            }
        }
    }
}