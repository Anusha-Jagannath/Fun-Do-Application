package com.example.fundo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundo.service.AuthenticationService

class ForgotPasswordViewModel: ViewModel() {

    private val _resetPasswordStatus = MutableLiveData<Boolean>()
    val resetPasswordStatus = _resetPasswordStatus as LiveData<Boolean>

    fun setResetPasswordStatus(status:Boolean) {
        _resetPasswordStatus.value = status
    }

    fun forgotPassword(email: String):Boolean {
        var result = false
        AuthenticationService().forgotPassword(email) { status, message ->
            if(status) {
                result = true
            }

        }
        return result
    }

}