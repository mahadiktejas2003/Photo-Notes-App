package com.dbtechprojects.photonotes

import com.dbtechprojects.photonotes.model.Note

object Constants {

    const val TABLE_NAME= "notes"
    const val DATABASE_NAME= "notesDatabase"

    //placeholder->this is for if we can't obtain the details for our note
    val noteDetailPlaceHolder= Note(

        note="Cannot find note details",
        id=0,
        title="Cannot find note details",
    )

    const val NAVIGATION_NOTES_CREATE="noteCreate"
    const val NAVIGATION_NOTES_LIST="noteList"
    const val NAVIGATION_NOTES_DETAIL="noteDetail/{noteId}"
    const val NAVIGATION_NOTES_EDIT="noteEdit/{noteId}"//make sure there are args. at the end of notes edit and notes detail page constants.
    const val NAVIGATION_NOTES_ID_ARGUMENT="noteId"
    //this is used in NoteDetail.kt
    fun noteDetailNavigation(noteId: Int)= "noteDetail/$noteId"
    fun noteEditNavigation(noteId: Int)= "noteEdit/$noteId"

}