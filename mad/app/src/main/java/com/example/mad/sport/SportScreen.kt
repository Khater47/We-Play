package com.example.mad.sport

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad.ui.theme.MadTheme


@Composable
fun SportScreen() {

    Scaffold(
        topBar = { TopAppBarSport() }
    ){
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            LazyColumn{
                items(30){
                    SportItem()
                }
            }
        }
    }


}

@Composable
fun TopAppBarSport() {


    TopAppBar(
        title = {
            Text(
                text = "Choose sport",
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
fun SportItem() {

//    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val rippleIndication = rememberRipple(bounded = true)
//    val route = BottomBarScreen.Reservation.route


    Row(Modifier
        .padding(10.dp)
        .clickable(
            interactionSource = interactionSource,
            indication = rippleIndication,
            onClick = {

            }
        )
        , verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Default.SportsSoccer,
            contentDescription = "SportIcon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = "Sport",
            style = MaterialTheme.typography.h5
        )
    }

    Divider()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        SportScreen()
    }
}