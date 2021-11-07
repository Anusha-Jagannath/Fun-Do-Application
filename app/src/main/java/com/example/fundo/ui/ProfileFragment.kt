package com.example.fundo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_main.*
import service.AuthenticationService
import viewmodels.SharedViewModel
import viewmodels.SharedViewModelFactory

class ProfileFragment : Fragment(R.layout.profilefragment) {

    lateinit var logout: Button
    lateinit var profile: TextView
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profilefragment, container, false)
        profile = view.findViewById(R.id.profileText)

        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory()
        )[SharedViewModel::class.java]


        val args = this.arguments
//        val profileData = args?.get("name").toString()
//        profile.text = "Welcome $profileData"
        val email = args?.get("email")
        // var name = arguments?.get("name").toString()
        Log.d("Profile fragment", email.toString())
        profile.text = email.toString()

        logout = view.findViewById(R.id.logoutButton)
        logout.setOnClickListener {
            // FirebaseAuth.getInstance().signOut()

            AuthenticationService().logOut()
            Toast.makeText(context, "logout success", Toast.LENGTH_SHORT).show()
            sharedViewModel.setGotoLoginPageStatus(true)

        }
        return view
    }


}