package com.example.fundo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.fundo.R
import com.google.firebase.auth.FirebaseAuth
import model.UserDetails
import service.AuthenticationService

class RegisterFragment: Fragment(R.layout.registerfragment) {

    lateinit var loginText: TextView
    lateinit var userName: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var register: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.registerfragment, container, false)
        loginText = view.findViewById<TextView>(R.id.alreadyHaveAccount)
        userName = view.findViewById(R.id.inputUserName)
        email = view.findViewById(R.id.inputEmail)
        password = view.findViewById(R.id.userPassword)
        confirmPassword = view.findViewById(R.id.confirmPassword)



        register = view.findViewById(R.id.buttonRegister)
        register.setOnClickListener {

            if (Validation.checkCredentialsForRegister(
                    userName,
                    email,
                    password,
                    confirmPassword
                )
            ) {
                var emailId = email.editableText.toString()
                var pass = password.editableText.toString()

                AuthenticationService().register(emailId, pass) { status, message ->

                    if (status) {
                        var newUser = UserDetails(userName.text.toString(),email.text.toString(),true)
                        var bundle = Bundle()
                        bundle = Validation.addInfoToBundle(newUser)
                        var profileFragment = ProfileFragment()
                        profileFragment.arguments = bundle
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainer, profileFragment)
                            commit()
                        }
                    }

                    else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        loginText.setOnClickListener {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()

            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, LoginFragment())
                commit()
            }


        }


        return view
    }
}


//    private fun checkCredentials() {
//        var name = userName.editableText.toString()
//        var emailId = email.editableText.toString()
//        var pass = password.editableText.toString()
//        var confirmPass = confirmPassword.editableText.toString()
//
//        if(name.isEmpty() || name.length <3){
//            showError(userName,"Your user name is not valid")
//        }
//
//        else if(emailId.isEmpty() || !emailId.contains('@')) {
//            showError(email,"Your email id not valid")
//        }
//        else if(pass.isEmpty() || pass.length <7) {
//            showError(password,"password not valid")
//        }
//        else if(confirmPass.isEmpty() || confirmPass.equals(pass)) {
//            showError(confirmPassword ,"password not matched")
//        }
//        else {
//            Toast.makeText(context,"Call registration method",Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun showError(input: EditText, s: String) {
//        input.setError(s)
//        input.requestFocus()
//
//    }


