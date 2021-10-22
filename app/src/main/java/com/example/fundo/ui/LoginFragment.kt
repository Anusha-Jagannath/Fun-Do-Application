package com.example.fundo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.fundo.R
import com.google.firebase.auth.FirebaseAuth
import service.AuthenticationService

class LoginFragment : Fragment(R.layout.loginfragment) {

    lateinit var signUpText: TextView
    lateinit var inputEmail: EditText
    lateinit var password: EditText
    lateinit var login: Button

    lateinit var forgotPass:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.loginfragment, container, false)
        forgotPass = view.findViewById(R.id.forgotPassword)
        signUpText = view.findViewById(R.id.textViewSignUp)
        inputEmail = view.findViewById(R.id.emailID)
        password = view.findViewById(R.id.inputPassword)
        login = view.findViewById(R.id.buttonLogin)
        login.setOnClickListener {

            if (Validation.checkCrendentialsForLogin(inputEmail, password)) {
                var email = inputEmail.editableText.toString()
                var password = password.editableText.toString()
                var profileFragment = ProfileFragment()
                val bundle = Bundle()
                bundle.putString("data",email)
                profileFragment.arguments = bundle

                AuthenticationService().login(email,password) { status,message ->
                    Toast.makeText(context,"$status",Toast.LENGTH_SHORT).show()
                    if(status) {
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


        signUpText.setOnClickListener {
            Toast.makeText(context, "sign clicked", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, RegisterFragment())
                commit()
            }
        }


        forgotPass.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, ForgotPasswordFragment())
                commit()
                Toast.makeText(context, "forgot password clicked", Toast.LENGTH_SHORT).show()
            }




        }
            return view
        }


//    private fun checkCrendentials() {
//        var emailId = inputEmail.editableText.toString()
//        var pass = password.editableText.toString()
//
//        if(emailId.isEmpty() || !emailId.contains('@')) {
//            showError(inputEmail,"Your email id not valid")
//        }
//        else if(pass.isEmpty() || pass.length <7) {
//            showError(password,"password not valid")
//        }
//
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

}