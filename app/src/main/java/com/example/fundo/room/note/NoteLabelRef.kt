package com.example.fundo.room.note

import androidx.room.Entity

@Entity(
    primaryKeys = ["noteid","labelId"]
)
data class NoteLabelRef(
    val noteid:Int,
    val labelId:Int
)