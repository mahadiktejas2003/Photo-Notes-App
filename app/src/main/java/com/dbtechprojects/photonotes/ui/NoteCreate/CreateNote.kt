package com.dbtechprojects.photonotes.ui.NoteCreate

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dbtechprojects.photonotes.NotesViewModel
import com.dbtechprojects.photonotes.PhotoNotesApp
import com.dbtechprojects.photonotes.R
import com.dbtechprojects.photonotes.model.Note
import com.dbtechprojects.photonotes.ui.GenericAppBar
import com.dbtechprojects.photonotes.ui.NotesList.NotesFab
import com.dbtechprojects.photonotes.ui.theme.PhotoNotesTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateNoteScreen(

    navController: NavController,
    viewModel: NotesViewModel
){



//find properties for each individual object within the note .

    val currentNote = remember{
        mutableStateOf("")
    }

    val currentTitle = remember{
        mutableStateOf("")
    }

    val currentPhotos= remember{
        mutableStateOf("")
    }

    val saveButtonState= remember{
        mutableStateOf(false)
        //false as we initially don't want to show that button
//but if anything changes in notesDetails then we wanna show the button
    }
//the below is activity result contract, that we'll launch so that user can go into their gallary and select a photo, then we can use that photo in our notes
    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->

        if (uri != null) {
            PhotoNotesApp.getUriPermission(uri)
        }
       currentPhotos.value=uri.toString()
    }

    //now layout the ui
    //copy the appbard from NoteDetail.kt ie. prev composable nd change acc.

    PhotoNotesTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Create Note",

                        onIconClick = {
                            viewModel.createNote(
                               currentTitle.value,
                                currentNote.value,
                                currentPhotos.value
                            )
                            navController.popBackStack()
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = stringResource(R.string.save_note),
                                tint = Color.Black,
                            )
                        },
                        iconState = saveButtonState
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = stringResource(R.string.add_photo),
                        action = {
                            getImageRequest.launch(arrayOf("image/*"))

                        },
                        icon = R.drawable.camera
                    )
                },
                content = {

                    Column(
                        Modifier
                            .padding(12.dp)
                            .fillMaxSize()
                    ) {
                        if (currentPhotos.value.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(data = Uri.parse(currentPhotos.value))
                                        .build()
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.3f)
                                    .padding(6.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        TextField(
                            value = currentTitle.value,
                            modifier=Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            onValueChange = { value ->
                                currentTitle.value = value
                              saveButtonState.value=currentTitle.value!=""&& currentNote.value!= ""
                            },
                            label = { Text(text = "Title") }
                        )
                        Spacer(modifier = Modifier.padding(12.dp))
                        TextField(
                            value = currentNote.value,
                            modifier= Modifier.
                                fillMaxHeight(0.5f)//fills half height of screen
                                .fillMaxWidth(),

                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            onValueChange = { value ->
                                currentNote.value = value
                                saveButtonState.value=currentTitle.value!=""&& currentNote.value!= ""

                            },
                            label = { Text(text = "Body") }
                        )
                    }
                }

            )
        }
    }
}