package com.example.fundo.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fundo.room.user.User

@Dao
interface UserDAO {
    @Insert
    suspend fun insert(user: User)

    @Query("delete from User where uid=:uid")
    suspend fun delete(uid:Int)

    @Update
    suspend fun update(user: User)

    @Query("select * from User")
    suspend fun display():List<User>
}