package com.example.fundo.ui

import android.widget.EditText

object Validation {

    fun checkCredentialsForRegister(userName: EditText, email: EditText, password: EditText, confirmPassword: EditText):Boolean {
        var name = userName.editableText.toString()
        var emailId = email.editableText.toString()
        var pass = password.editableText.toString()
        var confirmPass = confirmPassword.editableText.toString()

        if(name.isEmpty() || name.length <3){
            showError(userName,"Your user name is not valid")
            return false
        }

        else if(emailId.isEmpty() || !emailId.contains('@')) {
            showError(email,"Your email id not valid")
            return false
        }
        else if(pass.isEmpty() || pass.length <7) {
            showError(password,"password length should be > 7")
            return false
        }
        else if(confirmPass.isEmpty() || !confirmPass.equals(pass)) {
            showError(confirmPassword ,"password not matched")
            return false
        }
        return true
    }





    private fun showError(input: EditText, s: String) {
        input.setError(s)
        input.requestFocus()

    }


    fun checkCrendentialsForLogin(inputEmail:EditText,password: EditText):Boolean {
        var emailId = inputEmail.editableText.toString()
        var pass = password.editableText.toString()

        if(emailId.isEmpty() || !emailId.contains('@')) {
            showError(inputEmail,"Your email id not valid")
            return false
        }
        else if(pass.isEmpty() || pass.length <7) {
            showError(password,"password not valid")
            return false
        }

       return true
    }



    fun validateEmail(inputEmail:EditText):Boolean {
        var emailId = inputEmail.editableText.toString()

        if(emailId.isEmpty() || !emailId.contains('@')) {
            showError(inputEmail,"Your email id not valid")
            return false
        }
        return true

    }

    }