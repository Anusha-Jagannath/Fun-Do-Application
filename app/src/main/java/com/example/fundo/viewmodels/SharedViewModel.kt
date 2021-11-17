package com.example.fundo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundo.model.UserDetails

class SharedViewModel : ViewModel() {
    private val _gotoHomePageStatus = MutableLiveData<UserDetails>()
    val gotoHomePageStatus = _gotoHomePageStatus as LiveData<UserDetails>

    private val _gotoRegisterPageStatus = MutableLiveData<Boolean>()
    val gotoRegisterPageStatus = _gotoRegisterPageStatus as LiveData<Boolean>

    private val _gotoLoginPageStatus = MutableLiveData<Boolean>()
    val gotoLoginPageStatus = _gotoLoginPageStatus as LiveData<Boolean>

    private val _gotoForgotPageStatus = MutableLiveData<Boolean>()
    val gotoForgotPageStatus = _gotoForgotPageStatus as LiveData<Boolean>


    fun setGotoHomePageStatus(userDetails: UserDetails) {
        _gotoHomePageStatus.value = userDetails
    }

    fun setGotoRegisterPageStatus(status: Boolean) {
        _gotoRegisterPageStatus.value = status
    }

    fun setGotoLoginPageStatus(status: Boolean) {
        _gotoLoginPageStatus.value = status
    }

    fun setGotoForgotPageStatus(status: Boolean) {
        _gotoForgotPageStatus.value = status
    }

//    fun registerUser(name: String, email: String, password: String): Boolean {
//        var result = false
//        AuthenticationService().register(email, password) { status, message ->
//            Log.d("share", "$status")
//            if (status) {
//                result = true
//            } else
//                result = false
//        }
//        return result
//    }

//    fun loginUser(email: String, password: String) :Boolean{
//        var result = false
//        AuthenticationService().login(email, password) { status, message ->
//            if (status) {
//                result = true
//            } else {
//                result = false
//            }
//        }
//        return result
//
//    }


//    fun forgotPassword(email: String):Boolean {
//        var result = false
//        AuthenticationService().forgotPassword(email) { status, message ->
//            if(status) {
//                result = true
//            }
//
//        }
//        return result
//    }

}