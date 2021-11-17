package com.example.fundo.room.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotesEntity")
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    val noteid: Int?,
    val fid:String?,
    val title: String?,
    val content: String?,
    val date: String?
)