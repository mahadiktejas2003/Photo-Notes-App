package com.dbtechprojects.photonotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dbtechprojects.photonotes.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName= Constants.TABLE_NAME, indices=[Index(value=["id"],unique=true)])
data class Note(

    @PrimaryKey(autoGenerate=true) val id:Int?=null,
    @ColumnInfo(name="note") val note:String,
    @ColumnInfo(name="title") val title:String,
    @ColumnInfo(name="dateUpdated") val dateUpdated: String=getDateCreated(),
    @ColumnInfo(name="imageUri") val imageUri:String?=null


    )

fun getDateCreated():String{

    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

}

fun Note.getDay(): String {

    if(this.dateUpdated.isEmpty()) return ""
    val formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(this.dateUpdated, formatter).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}
//a dummy list for the fxn = orPlaceHolderList fxn
val placeHolderList= listOf(Note(title="Cannot find note details",note="Cannot find note details",id=0))//add id=0 means that we're unable to click on placeholder in the list as we don't want to be clicked on


fun List< Note>?.orPlaceHolderList(): List<Note> {
    return if (this != null && this.isNotEmpty()) {

        this
    } else {
        placeHolderList
    }
}

        //here this means List