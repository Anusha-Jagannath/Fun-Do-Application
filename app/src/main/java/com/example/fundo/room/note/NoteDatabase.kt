package com.example.fundo.room.note

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteKey::class,Label::class,NoteLabelRef::class],version = 1)
abstract class NoteDatabase:RoomDatabase() {
    abstract fun noteDao(): NoteDAO
    companion object{
        var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java,
                    "NoteKey.db").build()
            }
            return INSTANCE!!
        }
    }
}