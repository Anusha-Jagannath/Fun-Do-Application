package com.example.fundo.networking.users

import retrofit2.http.*

interface UsersApi {

    @GET("projects/registration-f48e2/databases/(default)/documents/users")
    suspend fun getUsers(): FirebaseUsersResponse

    @POST("projects/registration-f48e2/databases/(default)/documents/users")
    suspend fun postUser(): FirebaseUsersResponse

    @GET("projects/registration-f48e2/databases/(default)/documents/users/{userid}")
    suspend fun getSingleUser(@Path("userid") userId: String) : UserDocument

    @POST("projects/registration-f48e2/databases/(default)/documents/users")
    suspend fun addUser(@Query("documentId") userId: String, @Body userDocument: UserAddDocument)
}