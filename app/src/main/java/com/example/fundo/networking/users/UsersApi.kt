package com.example.fundo.networking.users

import retrofit2.http.GET

interface UsersApi {

    @GET("projects/registration-f48e2/databases/(default)/documents/users")
    suspend fun getUsers(): FirebaseUsersResponse
}