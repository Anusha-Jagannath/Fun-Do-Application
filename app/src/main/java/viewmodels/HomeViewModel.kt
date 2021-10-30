package viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.UserDetails
import service.AuthenticationService

class HomeViewModel: ViewModel() {


    fun logout() {
        AuthenticationService().logOut()
    }


    fun getUid(): String {
        return AuthenticationService().getUid()
    }
}