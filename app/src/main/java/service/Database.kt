package service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import model.UserDetails

class Database {

    fun saveUserData(user: UserDetails) {
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Database", "successfully added")
                }
            }
    }


    fun getData(): String {
        var fullName = ""
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {

                if (it.exists()) {
                    fullName = it.child("userName").value.toString()
                }
            }
        return fullName
    }


}