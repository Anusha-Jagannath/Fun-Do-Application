package com.example.fundo.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.MainActivity2
import com.example.fundo.R
import com.example.fundo.databinding.ActivityMainBinding
import model.UserDetails
import viewmodels.SharedViewModel
import viewmodels.SharedViewModelFactory

open class MainActivity : AppCompatActivity(){

    //lateinit var facebookButton: ImageView
    lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        //replaceFragment(LoginFragment())

        //facebookButton = findViewById(R.id.facebookbtn)

        sharedViewModel = ViewModelProvider(this@MainActivity, SharedViewModelFactory())[SharedViewModel::class.java]
        observeAppNavigation()
        gotoLoginPage()



        Log.i("MainActivity","App status : on create")


    }


    private fun gotoLoginPage() {
        replaceFragment(LoginFragment())
    }

    private fun gotoRegistrationPage() {
        replaceFragment(RegisterFragment())
    }

    private fun gotoProfilePage(profileFragment: ProfileFragment) {
        replaceFragment(profileFragment)
    }

    private fun gotoForgotPage() {
        replaceFragment(ForgotPasswordFragment())
    }

    private fun gotoHomeActivity() {
        var intent = Intent(this@MainActivity,HomeActivity::class.java)
        startActivity(intent)
    }

    private fun observeAppNavigation() {
        sharedViewModel.gotoHomePageStatus.observe(this@MainActivity,{
            if(it.loginStatus) {
                Log.d("MainFragment",it.email)
                var bundle = Validation.addInfoToBundle(it)
                var profileFragment = ProfileFragment()
                profileFragment.arguments = bundle
                //gotoProfilePage(profileFragment)
                gotoHomeActivity()
            }
        })


        sharedViewModel.gotoRegisterPageStatus.observe(this@MainActivity,{
            if(it) {
                gotoRegistrationPage()
            }
        })

        sharedViewModel.gotoLoginPageStatus.observe(this@MainActivity,{
            if(it) {
                gotoLoginPage()
            }
        })

        sharedViewModel.gotoForgotPageStatus.observe(this@MainActivity,{
            if(it) {
                gotoForgotPage()
            }
        })

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