package com.example.mad.reservation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mad.ui.theme.MadTheme



@Composable
fun ReservationScreen() {
    Box(Modifier.fillMaxSize()) {
        DateContainer()
    }
}

//Text(
//text = "ReservationScreen",
//modifier = Modifier.align(Alignment.Center),
//style = MaterialTheme.typography.h4
//)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ReservationScreen()
    }
}

@Composable
fun DateContainer(){
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

    }
}