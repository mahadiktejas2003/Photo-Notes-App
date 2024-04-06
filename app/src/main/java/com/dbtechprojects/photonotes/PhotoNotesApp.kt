package com.dbtechprojects.photonotes

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.net.Uri

import androidx.room.Room
import com.dbtechprojects.photonotes.persistence.NotesDao
import com.dbtechprojects.photonotes.persistence.NotesDatabase

class PhotoNotesApp: Application() {

    private var db: NotesDatabase?=null

    init{
        instance = this

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getDb():NotesDatabase {
        //this retrieved db in MainActivity.

        if(db!=null){

            return db!!
        }
        else{
         db= Room.databaseBuilder(
                instance!!.applicationContext,
                NotesDatabase:: class.java,
                Constants.DATABASE_NAME


            ).fallbackToDestructiveMigration().build()

                return db!!
        }
        }

    companion object{
        private var instance: PhotoNotesApp?= null

        fun getDao(): NotesDao {

            return instance!!.getDb().NotesDao()
        }

        //method for getting uri permission.-for the photos the user chooses,


        fun getUriPermission(uri: Uri){
            instance!!.applicationContext.contentResolver.takePersistableUriPermission(

                uri,
                 Intent.FLAG_GRANT_READ_URI_PERMISSION

            )
        }


    }


}