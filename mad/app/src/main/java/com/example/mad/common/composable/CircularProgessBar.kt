package com.example.mad.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar(
    isDisplayed:Boolean
){
    if(isDisplayed){
        Row(
            modifier= Modifier
                .fillMaxWidth()
                .padding(50.dp),
            horizontalArrangement = Arrangement.Center,
        ){
            CircularProgressIndicator(
                color= Color.Black
            )
        }
    }
}