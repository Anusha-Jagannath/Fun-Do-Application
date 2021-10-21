package com.example.fundo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fundo.R
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(R.layout.profilefragment){

    lateinit var logout: Button
    lateinit var profile:TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profilefragment, container, false)
        profile = view.findViewById(R.id.profileText)
        val args = this.arguments
        val profileData = args?.get("data")
        profile.text = "Welcome $profileData"
        logout = view.findViewById(R.id.logoutButton)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(context,"logout success",Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer,LoginFragment())
                commit()
            }
        }
        return view
    }
}