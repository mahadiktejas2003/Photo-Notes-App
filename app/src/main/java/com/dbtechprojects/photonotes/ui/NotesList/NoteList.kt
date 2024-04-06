package com.dbtechprojects.photonotes.ui.NotesList

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.Query
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dbtechprojects.photonotes.Constants
import com.dbtechprojects.photonotes.NotesViewModel
import com.dbtechprojects.photonotes.PhotoNotesApp
import com.dbtechprojects.photonotes.R
import com.dbtechprojects.photonotes.model.Note
import com.dbtechprojects.photonotes.model.getDay
import com.dbtechprojects.photonotes.model.orPlaceHolderList
import com.dbtechprojects.photonotes.ui.GenericAppBar
import com.dbtechprojects.photonotes.ui.theme.PhotoNotesTheme
import com.dbtechprojects.photonotes.ui.theme.noteBGBlue
import com.dbtechprojects.photonotes.ui.theme.noteBGYellow


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteListScreen(

    navController: NavController,
    viewModel: NotesViewModel
) {


//find properties for each individual object within the note .

    val deleteText = remember {
        mutableStateOf("")
    }
//query that use will type into searchbar
    val noteQuery = remember {
        mutableStateOf("")
    }

    val notesToDelete = remember {
        mutableStateOf(listOf<Note>())
    }
//show the delete dialogue
    val openDialog = remember {
        mutableStateOf(false)
    }

    val notes=viewModel.notes.observeAsState()
val context= LocalContext.current

    //now layout the ui
    //copy the appbard from NoteDetail.kt ie. prev composable nd change acc.

    PhotoNotesTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = stringResource(id = R.string.photo_notes),

                        //delete button logic
                        onIconClick = {
                            if(notes.value?.isNotEmpty()==true){

                                //then set the open dialog to  show our delete dialog
                                openDialog.value=true
                                //deleting all notes
                                deleteText.value="Are you sure you want to delete all notes"
                                notesToDelete.value=notes.value?: emptyList()

                            }
                            else{//otherwie show toast msg
                                Toast.makeText(context, "No notes found",Toast.LENGTH_SHORT).show()
                                //.show () is to show the msg

                            }


                            navController.popBackStack()
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.note_delete),
                                contentDescription = stringResource(R.string.delete_note),
                                tint = Color.Black,
                            )
                        },
                        iconState = remember{
                            mutableStateOf(true)
                        }
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = stringResource(R.string.create_note),
                        action = {
                                //use nav ctrlr to navigate to  note create screen
                                 //and use one of our root->constandt.nav..
                                 navController.navigate(Constants.NAVIGATION_NOTES_CREATE)
                        },
                        icon = R.drawable.note_add_icon
                    )
                }
            )
            {

                Column {
                    SearchBar(query= noteQuery)
                    NotesList(
                        notes = notes.value.orPlaceHolderList(),

                        //Now add a fxn to check whether if the list  is Null->if it's null  we'll show a placeholder
                        //if it's not then show the value of the notes.=> make the fxn orPlaceHolderList()  in the Note data class as it will be another extension fxn
                        opendialog =openDialog  ,
                        query =noteQuery,
                        deleteText = deleteText,
                        navController = navController,
                        notesToDelete =notesToDelete
                    )
                }
//add the delete dialog as this will show across whole screen
                
                DeleteDialog(openDialog = openDialog,
                    text =deleteText ,
                    action = {
                             notesToDelete.value.forEach(){
                                //loop for all notes
                                 viewModel.deleteNote(it)

                                     //it =means that note is passed in

                             }

                         }, notesToDelete =notesToDelete )

            }
        }

    }
}



//create individual items of the page- search bar, notes fab, and notes list

//1. Search bar
@Composable
fun SearchBar(query: MutableState<String>){
    Column(Modifier.padding(top=12.dp,start=12.dp,end=12.dp,bottom=0.dp)){
        TextField(

            value =query.value ,
            placeholder={ Text(text="Search..")},
            maxLines = 1,// to have only 1 lines text in search bar
            onValueChange ={//this assigns whatever user types in query box
                query.value=it

            },
            modifier= Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(12.dp))//search bar to have rounded corners
                .fillMaxWidth(),
            colors=TextFieldDefaults.textFieldColors(
                textColor = Color.Black
            ),
//trailing icon for search bar clear button
            trailingIcon ={
                //animatedvisib.->bcz we want the clear button to appear only when search bar is being used
                AnimatedVisibility(visible =query.value.isNotEmpty(),enter= fadeIn(),exit= fadeOut() ) {
                    IconButton(onClick = { query.value=""}) {

                        Icon(

                            imageVector=ImageVector.vectorResource(id=R.drawable.icon_cross) ,
                            contentDescription = stringResource(id=R.string.clear_search)
                        )

                    }

                }
            }
        )


    }
}


//Notes List

@Composable
fun NotesList(


    notes:List<Note>,
    opendialog:MutableState<Boolean>,
    query: MutableState<String>,
    deleteText:MutableState<String>,
    navController: NavController,
    notesToDelete: MutableState<List<Note>>
){

    //we wanna have one header for each day and if note is created in same day we don't want to create second header for that

    var previousHeader = ""

    LazyColumn (
        contentPadding= PaddingValues(12.dp), modifier=Modifier.background(MaterialTheme.colors.primary))

    {
        //show what notes we wanna show
        //if the user types the query, we wanna filter the list to that query otherwise we want to show list of whole list of notes, so we'll define that here

        val queriedNotes=
            if(query.value.isEmpty()){
                notes//show notes in list
        }
        else{
            //otherwise filter the nots
            notes.filter{
                it.note.contains(query.value)||it.title.contains(query.value) }
            }
        //use index method as we wanna access the index of our items
        itemsIndexed(queriedNotes){index,note->

            //now we wanna check whether the date the note was created is different to our previos day= ie. previousHeader=>for this create the extension fxn so that u can use it on every note
            //so go to Note data class->and create .getDay()
            if (note.getDay() != previousHeader) {
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = note.getDay(), color = Color.Black)
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )

                previousHeader=note.getDay()
            }

            NoteListItem(
                note,
                opendialog,
                deleteText,
                navController,
                //bg colors:=> if the position is even we want it to be yellow, elsewise  use blue color
                 if (index % 2 == 0) {
                    noteBGYellow
                } else{ noteBGBlue},
                notesToDelete ,
                )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
        }
    }
}



@OptIn( ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    note: Note,
    openDialog: MutableState<Boolean>,
    deleteText: MutableState<String>,
    navController: NavController,
    noteBackGround: Color,
    notesToDelete: MutableState<List<Note>>
) {

    return Box(modifier = Modifier
        .height(120.dp)
        .clip(RoundedCornerShape(12.dp))) {
        Column(
            modifier = Modifier
                .background(noteBackGround)
                .fillMaxWidth()
                .height(120.dp)

                //we want the list item to be both clickable in terms of a long press for deleting the item
                //and also nomal press for navigating the note detail page=>Using Combined clickable method
                .combinedClickable(interactionSource = remember { MutableInteractionSource() },

                    //few details:

                    //add the ripple effect
                    indication = rememberRipple(bounded = false), // You can also change the color and radius of the ripple
                    onClick = {
                        if (note.id != 0) {
                            navController.navigate(Constants.noteDetailNavigation(note.id ?: 0))
                        }
                    },
                    //then for the long click-wanna set deleting of notes
                    onLongClick = {
                        if (note.id != 0) {
                            openDialog.value = true
                            deleteText.value = "Are you sure you want to delete this note ?"
                            notesToDelete.value = mutableListOf(note)
                        }
                    }
                )

        ) {
            Row(){

                //check if this note has an image or not , and if it does show it
                if (note.imageUri != null && note.imageUri.isNotEmpty()){
                    // load firs image into view
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(data = Uri.parse(note.imageUri))
                                .build()
                        ),
                        contentDescription = null,//we don't know what'll be image
                        modifier = Modifier
                            .fillMaxWidth(0.3f),//fills the 3rd width of the item so 0.3
                        contentScale = ContentScale.Crop
                    )
                }

                Column() {
                    Text(
                        text = note.title,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)//as we want 12 dp away from the image

                    )
                    Text(
                        //for body
                        text = note.note,
                        color = Color.Black,
                        maxLines = 3,
                        modifier = Modifier.padding(12.dp)//12 dp all around
                    )
                    Text(
                        text = note.dateUpdated,//when note was last updated
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

        }
    }
}



//fab button.
//action=action that'll happen when button is clicked
@Composable
fun NotesFab(contentDescription: String, icon: Int, action: () -> Unit) {
    return FloatingActionButton(
        onClick = { action.invoke() },
        backgroundColor = MaterialTheme.colors.primary//means black
    ) {
        Icon(
            ImageVector.vectorResource(id = icon),
            contentDescription,
            tint = Color.Black
        )

    }
}


//Delete Dialog
@Composable
fun DeleteDialog(
    openDialog: MutableState<Boolean>,
    text: MutableState<String>,
    action: () -> Unit,
    notesToDelete: MutableState<List<Note>>
) {//if the opendialog value is true->then set  an alert dialog
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Delete Note")
            },
            text = {//delete text
                Column() {
                    Text(text.value)//text widget
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column() {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            //button colors
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                action.invoke()
                                openDialog.value = false//set dialog value to false
                                notesToDelete.value = mutableListOf()//set the notes to an empty list(i.e mutablelistof())
                            }
                        ) {
                            Text("Yes")
                        }
                        Spacer(modifier = Modifier.padding(12.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                openDialog.value = false
                                notesToDelete.value = mutableListOf()
                            }
                        ) {
                            Text("No")
                        }
                    }

                }
            }
        )
    }
}