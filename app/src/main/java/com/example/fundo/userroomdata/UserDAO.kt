package com.example.fundo.userroomdata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDAO {
    @Insert
    suspend fun insert(user:User)

    @Query("delete from User where uid=:uid")
    suspend fun delete(uid:Int)

    @Update
    suspend fun update(user: User)

    @Query("select * from User")
    suspend fun display():List<User>
}