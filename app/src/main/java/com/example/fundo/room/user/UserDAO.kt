package com.example.fundo.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fundo.room.user.User

@Dao
interface UserDAO {
    @Insert
    fun insert(user: User)

    @Query("delete from User where uid=:uid")
    fun delete(uid:Int)

    @Update
    fun update(user: User)

    @Query("select * from User")
    fun display():List<User>
}