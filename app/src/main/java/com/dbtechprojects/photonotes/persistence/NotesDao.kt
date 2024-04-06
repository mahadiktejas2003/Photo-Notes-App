package com.dbtechprojects.photonotes.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dbtechprojects.photonotes.model.Note

@Dao
interface NotesDao {

    @Query("SELECT * FROM Notes WHERE notes.id=:id")
    suspend fun getNoteById(id: Int) : Note?

    //query to select all notes:
    //desc=descending


    // we'll return notes-

    //Livedata->means whenever a node is updated our db->that composable will update and show new state of our notes table
    @Query("SELECT * FROM Notes ORDER BY dateUpdated DESC")
    fun getNotes() : LiveData<List<Note>>


    //fxn to delete notes :
    @Delete
    fun deleteNote(note: Note) : Int


    //update the notes-so if we change any values from the note such as title ,body .for this use this fxn.
    @Update
    fun updateNote(note: Note) : Int

    @Insert
    fun insertNote(note:Note)


    //now move on to notes view model


}