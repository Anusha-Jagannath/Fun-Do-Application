package com.example.fundo.room.note

import androidx.room.*

@Dao
interface NoteDAO {
    @Insert
    suspend fun insert(note: NotesEntity)

    @Query("delete from NotesEntity where title=:title")
    suspend fun delete(title:String)

    @Update
    suspend fun update(note: NotesEntity)

    @Query("select * from NotesEntity")
    suspend fun display(): List<NotesEntity>
}