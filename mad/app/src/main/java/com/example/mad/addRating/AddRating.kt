package com.example.mad.addRating

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad.ui.theme.MadTheme


@Composable
fun AddRatingScreen() {

    Scaffold(
        topBar = { TopAppBarAddRating() }
    ){
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            LazyColumn{
                items(30){
                }
            }
        }
    }


}

@Composable
fun TopAppBarAddRating() {


    TopAppBar(
        title = {
            Text(
                text = " sport",
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {},
        elevation = 10.dp

    )
}

@Composable
fun EditItem() {


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        AddRatingScreen()
    }
}