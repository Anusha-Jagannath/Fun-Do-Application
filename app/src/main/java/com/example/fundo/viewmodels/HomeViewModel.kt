package com.example.fundo.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fundo.service.AuthenticationService

class HomeViewModel: ViewModel() {


    fun logout() {
        AuthenticationService().logOut()
    }


    fun getUid(): String {
        return AuthenticationService().getUid()
    }
}