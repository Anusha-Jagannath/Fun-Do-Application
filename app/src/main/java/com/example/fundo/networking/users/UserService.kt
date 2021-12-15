package com.example.fundo.networking.users

import android.util.Log
import com.example.fundo.networking.FirebaseUserDetails
import com.example.fundo.networking.RetrofitClient

class UserService {
    private val retrofit = RetrofitClient.createRetrofit()

    suspend fun getUsers(): ArrayList<FirebaseUserDetails> {
        val usersApi = retrofit.create(UsersApi::class.java)
        val userResponse = usersApi.getUsers()
        val userList: ArrayList<FirebaseUserDetails> = ArrayList(userResponse.documents.map {
            FirebaseUserDetails(
                it.fields.name.stringValue,
                it.fields.email.stringValue,
                it.fields.mobileNo.stringValue
            )
        })
        return userList
    }

    suspend fun getUser(): ArrayList<FirebaseUserDetails> {
        Log.d("TEST", "test")
        var userList: ArrayList<FirebaseUserDetails> = arrayListOf()
        val usersApi = retrofit.create(UsersApi::class.java)
        val userResponse = usersApi.getUser()
        if (userResponse.documents != null) {
            userList = ArrayList(userResponse.documents.map {
                FirebaseUserDetails(
                    it.fields.name.stringValue,
                    it.fields.email.stringValue,
                    it.fields.mobileNo.stringValue
                )
            })

        }
        return userList
    }
}