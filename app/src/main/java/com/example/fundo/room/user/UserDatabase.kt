package com.example.fundo.room.user

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fundo.ui.RegisterFragment

@Database(entities = [User::class],version=1)
abstract class UserDatabase:RoomDatabase() {
    abstract fun userDao(): UserDAO
    companion object{
        var INSTANCE: UserDatabase? = null

        fun getInstance(context: RegisterFragment): UserDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.requireActivity(),
                    UserDatabase::class.java,"User.db").build()
            }
            return INSTANCE!!
        }
    }
}