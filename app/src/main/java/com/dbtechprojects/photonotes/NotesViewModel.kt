package com.dbtechprojects.photonotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dbtechprojects.photonotes.model.Note
import com.dbtechprojects.photonotes.persistence.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//
class NotesViewModel(
//ctor
    val db: NotesDao

) : ViewModel() {


    val notes: LiveData<List<Note>> = db.getNotes()

    //fxn for delteing notes:

//provide  dispatcher.io to viewmodelscope->ViewModeScope launches a coroutine-

    fun deleteNote(note: Note) {

        viewModelScope.launch(Dispatchers.IO) {

            db.deleteNote(note)
        }
    }

    //update the note->very similar to deleteNote fxn-
    fun updateNote(note: Note) {

        viewModelScope.launch(Dispatchers.IO) {
            db.updateNote(note)
        }
    }


//we don't need to provide the date updated, as that'll be created  when we create a note

    fun createNote(title: String, note: String, image: String? = null) {

        val note = Note(title = title, note = note, imageUri = image)
        viewModelScope.launch(Dispatchers.IO) {
            db.insertNote(note)
        }
    }

    suspend fun getNote(id: Int): Note?
    {
        return db.getNoteById(id)

    }
    //class that'll provide viewmodel to our activity
    class NotesViewModelFactory(
        private val db: NotesDao
    ) : ViewModelProvider.NewInstanceFactory() {
//override the  onCreate  by pressing ctrl+o

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NotesViewModel(

                db = db
            ) as T

        }
    }
}