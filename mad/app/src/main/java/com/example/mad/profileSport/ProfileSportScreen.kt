package com.example.mad.profileSport

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mad.ui.theme.MadTheme


@Composable
fun ProfileSportScreen() {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = "Profile Sport Screen",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h4
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ProfileSportScreen()
    }
}