package com.example.mad.screens.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.DefaultImage
import com.example.mad.common.composable.FloatingButtonAdd
import com.example.mad.common.composable.Score
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextBasicIcon
import com.example.mad.common.composable.TextBasicTitle
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.common.getIconPlayground
import com.example.mad.common.getIconSport
import com.example.mad.ui.theme.MadTheme


@Composable
fun ProfileRatingScreen(
    navController: NavHostController,
//    vm: MainViewModel
) {
    fun goHome() {
        navController.navigate(BottomBarScreen.Home.route)
    }

    fun goToAddRating() {
//        val route = "/addRating"
//        navController.navigate(route)
    }

    Scaffold(
        topBar = { TopBarBackButton(R.string.topBarUserRating, ::goHome) },
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            PlaygroundRating()
        }
    }

}

@Composable
fun PlaygroundRating(
//    vm: MainViewModel
) {

    //ratingPlaygrounds

    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(4f)
                ) {
                    items(5) {
                        CardPlaygroundReservationPortrait()
                    }
                }
            }
        }

        else -> {
            Row(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    items(5) {
                        Row {
                            Column(Modifier.weight(1f)) {
                                CardPlaygroundReservationLandscape()
                            }
                            Column(
                                Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    InfoPlayground("Turin", "Soccer" )
                                }

                                Column(Modifier.padding(10.dp)) {
                                    InfoReservation("20/05/2023", "10:00" )
                                }
                            }

                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPlaygroundReservationLandscape() {

    val playgroundTitle = "Campo Admond"
    val playgroundSport = "Soccer"

    Card(
        onClick = {
            //ADD RATING
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Column(Modifier.padding(10.dp)) {
                TextBasicHeadLine(text = playgroundTitle)
            }
            DefaultImage(image = playgroundSport)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPlaygroundReservationPortrait() {

    val playgroundTitle = "Campo Admond"
    val playgroundSport = "Soccer"
    val playgroundLocation = "Turin"
    val playgroundDate = "20/05/2023"
    val playgroundTime = "10:00-11:00"

    Card(
        onClick = {
            //ADD RATING
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Column(Modifier.padding(10.dp)){
                TextBasicHeadLine(text = playgroundTitle)
            }
            DefaultImage(image = playgroundSport)
            InfoContainer(playgroundLocation, playgroundSport,playgroundDate,playgroundTime)

        }
    }
}

@Composable
fun InfoContainer(playgroundLocation: String, playgroundSport: String,playgroundDate:String,playgroundTime:String) {

    InfoPlayground(playgroundLocation,playgroundSport)
    InfoReservation(playgroundDate,playgroundTime)
}

@Composable
fun InfoPlayground(playgroundLocation: String, playgroundSport: String){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp)) {
        Column(Modifier.weight(1f)) {
            TextBasicIcon(playgroundLocation, Icons.Default.LocationOn)

        }
        Column(Modifier.weight(1f)) {
            TextBasicIcon(playgroundSport, getIconSport(playgroundSport))

        }
    }
}

@Composable
fun InfoReservation(playgroundDate:String,playgroundTime:String){

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp)) {
        Column(Modifier.weight(1f)) {
            TextBasicIcon(playgroundDate, Icons.Default.CalendarMonth)

        }
        Column(Modifier.weight(1f)) {
            TextBasicIcon(playgroundTime, Icons.Default.AccessTime)

        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewProfileRating() {
//    MadTheme {
//        ProfileRatingScreen()
//    }
//}
//
//
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewProfileRatingLandscape() {
//    MadTheme {
//        ProfileRatingScreen()
//    }
//}
