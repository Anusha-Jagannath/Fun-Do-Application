package com.example.fundo.room.note

import androidx.room.*

@Dao
interface NoteDAO {
    @Insert
    suspend fun insert(note: NoteKey)

    @Query("delete from NoteKey where title=:title")
    suspend fun delete(title:String)

    @Update
    suspend fun update(note: NoteKey)

    @Query("select * from NoteKey")
    suspend fun display(): List<NoteKey>

    @Insert
    fun insertLabel(label: Label)

    @Insert
    fun insertNoteLabelRef(ref:NoteLabelRef)

    @Transaction
    @Query("select * from NoteKey")
    fun getNotesWithLabels(): List<NotesWithLabels>

    @Transaction
    @Query("select * from Label")
    fun getLabelsWithNotes(): List<LabelsWithNotes>


    @Transaction
    @Query("select * from NoteKey where noteid=:noteid")
    fun getNotesWithLabelsById(noteid: Int): List<NotesWithLabels>

    @Transaction
    @Query("select * from Label where labelId=:labelId")
    fun getlabelsWithNotesById(labelId:Int): List<LabelsWithNotes>

    @Query("select * from NoteKey LIMIT:limit OFFSET:offset")
    fun getPagedNotes(limit: Int,offset: Int):List<NoteKey>

}