package com.example.fundo.room.note

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NotesWithLabels(
    @Embedded val notesEntity: NotesEntity,
    @Relation(
        parentColumn = "noteid",
        entityColumn = "labelId",
        associateBy = Junction(NoteLabelRef::class)
    )
    val songs: List<Label>
)

data class LabelsWithNotes(
    @Embedded val label: Label,
    @Relation(
        parentColumn = "labelId",
        entityColumn = "noteid",
        associateBy = Junction(NoteLabelRef::class)
    )
    val playlists: List<NoteKey>
)