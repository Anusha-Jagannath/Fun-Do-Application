package service

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class AuthenticationService {

    fun forgotPassword(email: String,listener:(Boolean,String) ->Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                    if(it.isSuccessful) {
                       listener(true,"Check your email id for reset password")
                    }
                    else {
                       listener(false,it.exception?.message.toString())
                    }
                }
    }

    fun register(email:String,password:String,listener: (Boolean, String) -> Unit) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if(it.isSuccessful) {
                           listener(true,"registration is successful")
                        }
                        else {
                           listener(false,it.exception?.message.toString())
                        }
                    }

    }

    fun login(email:String,password:String,listener:(Boolean,String) -> Unit) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            listener(true,"login is successful")
                        } else {
                            listener(false,it.exception?.message.toString())
                        }
                    }

    }
}