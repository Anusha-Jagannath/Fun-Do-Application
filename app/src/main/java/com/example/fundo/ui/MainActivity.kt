package com.example.fundo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.fundo.R
import com.example.fundo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        replaceFragment(LoginFragment())



        Log.i("MainActivity","App status : on create")
    }

    public fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
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