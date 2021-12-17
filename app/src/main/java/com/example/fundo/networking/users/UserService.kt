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

    suspend fun getSingleUser(userId: String): FirebaseUserDetails {
        val usersApi: UsersApi = retrofit.create(UsersApi::class.java)
        val userResponse = usersApi.getSingleUser(userId)
        val fields = userResponse.fields

        return FirebaseUserDetails(
            fields.name.stringValue,
            fields.email.stringValue,
            fields.mobileNo.stringValue
        )
    }

    suspend fun addUser(user: Users): Users {
        val retrofit = RetrofitClient.createRetrofit()
        val usersApi: UsersApi = retrofit.create(UsersApi::class.java)
        val userField = UserField(
            name = StringField(user.name.toString()),
            email = StringField(user.email.toString()),
            phone = StringField(user.mobileNo.toString())
        )
        val userAddDocument = UserAddDocument(userField)

        usersApi.addUser(user.fid.toString(), userAddDocument)
        return user
    }
}