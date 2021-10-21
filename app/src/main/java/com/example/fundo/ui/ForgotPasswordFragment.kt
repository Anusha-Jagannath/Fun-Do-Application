package com.example.fundo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fundo.R
import com.google.firebase.auth.FirebaseAuth
import service.AuthenticationService

class ForgotPasswordFragment : Fragment() {

    lateinit var inputEmail: EditText
    lateinit var resetButton: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.forgotpasswordfragment, container, false)
        inputEmail = view.findViewById(R.id.inputEmailId)
        resetButton = view.findViewById(R.id.resetBtn)
        resetButton.setOnClickListener {
            if (Validation.validateEmail(inputEmail)) {
                var emailId = inputEmail.editableText.toString()
                AuthenticationService().forgotPassword(emailId) { status, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }
}