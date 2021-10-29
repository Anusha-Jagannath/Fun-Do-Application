package viewmodels

import androidx.lifecycle.ViewModel
import service.AuthenticationService

class HomeViewModel: ViewModel() {

    fun logout() {
        AuthenticationService().logOut()
    }


    fun getUid(): String {
        return AuthenticationService().getUid()
    }
}