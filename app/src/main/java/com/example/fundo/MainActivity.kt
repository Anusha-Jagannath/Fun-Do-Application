package com.example.fundo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("MainActivity","App status : on create")
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity","App status : on start")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity","App status : on start")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity","App status : on pause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity","App status : on stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity","App status : on destroy")
    }
}