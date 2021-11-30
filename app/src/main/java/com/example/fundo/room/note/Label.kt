package com.example.fundo.room.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Label(
    @PrimaryKey(autoGenerate = true) val labelId: Int?,
    val labelName: String?
)