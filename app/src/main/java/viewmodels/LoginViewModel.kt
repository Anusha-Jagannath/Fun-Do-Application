package viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.UserDetails
import service.AuthenticationService
import service.Database

class LoginViewModel: ViewModel() {

    private val _loginStatus = MutableLiveData<UserDetails>()
    val loginStatus = _loginStatus as LiveData<UserDetails>


    fun setLoginStatus(userDetails: UserDetails) {
        _loginStatus.value = userDetails
    }


        fun loginUser(email: String, password: String) :Boolean{
        var result = false
        AuthenticationService().login(email, password) { status, message ->
            if (status) {
                result = true
            } else {
                result = false
            }

        }
        return result

    }


    fun readData():String
    {
        var database = Database()
        var fullName = database.getData()
        return fullName
    }


    }