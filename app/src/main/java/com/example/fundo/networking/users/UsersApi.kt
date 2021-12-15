package com.example.fundo.networking.users

import retrofit2.http.GET

interface UsersApi {

    @GET("projects/registration-f48e2/databases/(default)/documents/users")
    suspend fun getUsers(): FirebaseUsersResponse

    @GET("projects/registration-f48e2/databases/(default)/documents/users/EWGkgk69z4N8sQFtmlXt")
    suspend fun getUser(): FirebaseUsersResponse


}