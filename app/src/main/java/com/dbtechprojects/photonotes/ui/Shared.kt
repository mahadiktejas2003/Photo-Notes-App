package com.dbtechprojects.photonotes.ui

import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState


@Composable
fun  GenericAppBar(
    title:String,
    onIconClick:(()->Unit)?,
    icon: @Composable()(()->Unit?),
//iconstate- is to show or hide the icon
iconState: MutableState<Boolean>

    ){
    TopAppBar(title={ Text(
        title)},
        backgroundColor= MaterialTheme.colors.primary,
        actions={
            IconButton(onClick={ onIconClick?.invoke()},
//provide content for button

                //means if icon wants to be shown then we will show the icon|
                content= {
                    if (iconState.value){
                        icon.invoke()
                    }
                }
            )
        }

     )

}
