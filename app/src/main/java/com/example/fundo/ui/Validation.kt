package com.example.fundo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.fundo.R
import model.UserDetails

object Validation {

    fun checkCredentialsForRegister(
        userName: EditText,
        email: EditText,
        password: EditText,
        confirmPassword: EditText,context: Context
    ): Boolean {
        var name = userName.editableText.toString()
        var emailId = email.editableText.toString()
        var pass = password.editableText.toString()
        var confirmPass = confirmPassword.editableText.toString()

        if (name.isEmpty() || name.length < 3) {
            showError(userName, context.getString(R.string.error_username_invalid))
            return false
        } else if (emailId.isEmpty() || !emailId.contains('@')) {
            showError(email, "Your email id not valid")
            return false
        } else if (pass.isEmpty() || pass.length < 7) {
            showError(password, "password length should be > 7")
            return false
        } else if (confirmPass.isEmpty() || !confirmPass.equals(pass)) {
            showError(confirmPassword, "password not matched")
            return false
        }
        return true
    }


    private fun showError(input: EditText, s: String) {
        input.setError(s)
        input.requestFocus()

    }


    fun checkCrendentialsForLogin(inputEmail: EditText, password: EditText): Boolean {
        var emailId = inputEmail.editableText.toString()
        var pass = password.editableText.toString()

        if (emailId.isEmpty() || !emailId.contains('@')) {
            showError(inputEmail, "Your email id not valid")
            return false
        } else if (pass.isEmpty() || pass.length < 7) {
            showError(password, "password not valid")
            return false
        }

        return true
    }


    fun validateEmail(inputEmail: EditText): Boolean {
        var emailId = inputEmail.editableText.toString()

        if (emailId.isEmpty() || !emailId.contains('@')) {
            showError(inputEmail, "Your email id not valid")
            return false
        }
        return true

    }

    fun addInfoToBundle(newUser: UserDetails): Bundle {
        Log.d("Validation", newUser.email)
        var bundle = Bundle()
        bundle.putString("username", newUser.userName)
        bundle.putString("email", newUser.email)
        return bundle
    }


}