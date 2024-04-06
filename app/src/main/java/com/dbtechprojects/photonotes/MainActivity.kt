package com.dbtechprojects.photonotes

import NoteDetailPage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.dbtechprojects.photonotes.ui.NoteCreate.CreateNoteScreen


import com.dbtechprojects.photonotes.ui.NoteEdit.NoteEditScreen
import com.dbtechprojects.photonotes.ui.NotesList.NoteListScreen




class MainActivity : ComponentActivity() {
//lateinit value for our viewmodel and initilize it  in onCreate using viewmodel factory
    private lateinit var viewModel : NotesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // retrieve viewModel
        viewModel =  NotesViewModel.NotesViewModelFactory(PhotoNotesApp.getDao()).create(NotesViewModel::class.java)//call the create method and pass class -notesviewmodel


        setContent {
            //add navigation,
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Constants.NAVIGATION_NOTES_LIST//go create it in constants
            ) {
                // Notes List- first of set up notes list.
                //route is :Constants.NAVIGATION_NOTES_LIST
                composable(Constants.NAVIGATION_NOTES_LIST) {
                NoteListScreen(   navController=navController,
                    viewModel= viewModel
                )
                }

//Now set up notes detail page
                // Notes Detail page
                composable(
                    Constants.NAVIGATION_NOTES_DETAIL,
                    arguments = listOf(navArgument(Constants.NAVIGATION_NOTES_ID_ARGUMENT) {
                        type = NavType.IntType// it's int as it's the id of the note
                    })
                ) {//inside this fxn body we've access to backstack entry, and this is where we can get our notes Id
                    navBackStackEntry ->
                    navBackStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTES_ID_ARGUMENT)//key=Constants.NAVIGATION_NOTE_ID_Argument
                        ?.let {//now we wanna load the notes detail page
                            NoteDetailPage(noteId = it, navController = navController, viewModel=viewModel) }//prev=NoteDetailScreen  not NoteDetailPage
                }

                // Notes Edit page
                composable(
                    Constants.NAVIGATION_NOTES_EDIT,
                    arguments = listOf(navArgument(Constants.NAVIGATION_NOTES_ID_ARGUMENT) {
                        type = NavType.IntType
                    })
                ) { navBackStackEntry ->
                    navBackStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTES_ID_ARGUMENT)?.let {
                    NoteEditScreen(noteId = it, navController=navController,viewModel= viewModel) }//here it is by default=NoteEditScreen
                }

                // Create Note Page=>Final page
                //route is Constants.NAVIGATION_NOTES_CREATE
                    //no any arguments (in the composable() )
                composable(Constants.NAVIGATION_NOTES_CREATE) {
                    //load the CreateNoteScreen
                    CreateNoteScreen(navController=navController,viewModel= viewModel) }

            }

        }
    }
}
