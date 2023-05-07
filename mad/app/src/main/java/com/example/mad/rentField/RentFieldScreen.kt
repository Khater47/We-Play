package com.example.mad.rentField


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad.profileRating.TopAppBarRating
import com.example.mad.ui.theme.MadTheme


@Composable
fun RentFieldScreen() {


    Scaffold(
        topBar = { TopAppBarRentField() }
    ){
        Box(Modifier.fillMaxSize().padding(it)) {

        }
    }


}

@Composable
fun TopAppBarRentField() {


    TopAppBar(
        title = {
            Text(
                text = "Rent Field",
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        RentFieldScreen()
    }
}