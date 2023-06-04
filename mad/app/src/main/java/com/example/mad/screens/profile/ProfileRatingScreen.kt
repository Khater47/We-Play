package com.example.mad.screens.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.DefaultImage
import com.example.mad.common.composable.TextBasicIcon
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.common.getIconSport
import com.example.mad.model.Reservation
import com.example.mad.model.UserReservation
import com.example.mad.ui.theme.Gold

@Composable
fun ProfileRatingScreen(
    navController: NavHostController,
    vm: MainViewModel
) {
    fun goHome() {
        navController.navigate(BottomBarScreen.Home.route)
    }

    val loading = vm.loadingProgressBar.value

    val changeUi = remember {
        mutableStateOf(true)
    }

    val selectedReservation = remember {
        mutableStateOf(
            Reservation("","","","","",false,"","","","")
        )
    }

    val openDialog = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = changeUi.value){
        if(changeUi.value){
            vm.getUserToRatedPlayground()
            changeUi.value=false
        }

    }


    if(openDialog.value){
        FullDialogAddRating(openDialog,selectedReservation.value, vm,changeUi)
    }

    Scaffold(
        topBar = { TopBarBackButton(R.string.topBarUserRating, ::goHome) },
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            PlaygroundRating(vm,openDialog,selectedReservation)
            CircularProgressBar(isDisplayed = loading)
        }
    }

}

@Composable
fun PlaygroundRating(
    vm: MainViewModel,
    openDialog: MutableState<Boolean>,
    selectedReservation:MutableState<Reservation>
) {

    val reservations = vm.userReservation.observeAsState().value?: emptyList()

    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(4f)
                ) {
                    items(reservations) {item ->
                        CardPlaygroundReservationPortrait(item,selectedReservation,openDialog)
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
                    items(reservations) { item ->
                        Row {
                            Column(Modifier.weight(1f)) {
                                CardPlaygroundReservationLandscape(item,selectedReservation,openDialog)
                            }
                            Column(
                                Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)) {
                                    TextBasicIcon(text = item.address+" "+item.city, icon = Icons.Default.LocationOn)
                                }
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)) {
                                    TextBasicIcon(text = item.sport, icon = getIconSport(item.sport))
                                }

                                Column(Modifier.padding(horizontal = 10.dp)) {
                                    InfoReservation(item.date, item.startTime+"-"+item.endTime )
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
fun CardPlaygroundReservationLandscape(reservation:UserReservation, selectedReservation: MutableState<Reservation>, openDialog: MutableState<Boolean>) {

    val playgroundTitle = reservation.playground
    val playgroundSport = reservation.sport

    Card(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Row(Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(4f)) {
                    Text(playgroundTitle,style=MaterialTheme.typography.headlineSmall)
                }
                Column(Modifier.weight(1f)) {
                    IconButton(onClick = {
                        openDialog.value=true
                        selectedReservation.value=Reservation(
                            reservation.address,
                            reservation.city,
                            reservation.date,
                            "",
                            reservation.endTime,
                            reservation.equipment,
                            reservation.id,
                            reservation.playground,
                            reservation.sport,
                            reservation.startTime
                        )
                    }) {
                        Icon(imageVector = Icons.Default.Star,tint= Gold, contentDescription = "rateButton")
                    }
                }
            }
            DefaultImage(image = playgroundSport)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPlaygroundReservationPortrait(reservation:UserReservation, selectedReservation: MutableState<Reservation>, openDialog: MutableState<Boolean>) {

    val playgroundTitle = reservation.playground
    val playgroundSport = reservation.sport
    val playgroundLocation = reservation.address+" "+reservation.city
    val playgroundDate = reservation.date
    val playgroundTime = reservation.startTime+"-"+reservation.endTime

    Card(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Row(Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically){
                Column(Modifier.weight(4f)) {
                    Text(playgroundTitle,style=MaterialTheme.typography.headlineSmall)
                }
                Column(Modifier.weight(1f)) {
                    IconButton(onClick = {
                        openDialog.value=true
                        selectedReservation.value= Reservation(
                            reservation.address,
                            reservation.city,
                            reservation.date,
                            "",
                            reservation.endTime,
                            reservation.equipment,
                            reservation.id,
                            reservation.playground,
                            reservation.sport,
                            reservation.startTime
                        )
                    }) {
                        Icon(imageVector = Icons.Default.Star,tint= Gold,contentDescription = "rateButton")
                    }
                }
            }
            DefaultImage(image = playgroundSport)
            InfoContainer(playgroundLocation, playgroundSport,playgroundDate,playgroundTime)

        }
    }
}

@Composable
fun InfoContainer(playgroundLocation: String, playgroundSport: String,playgroundDate:String,playgroundTime:String) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)) {
        TextBasicIcon(text = playgroundLocation, icon = Icons.Default.LocationOn)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)) {
        TextBasicIcon(text = playgroundSport, icon = getIconSport(playgroundSport))
    }
    InfoReservation(playgroundDate,playgroundTime)
}

//@Composable
//fun InfoPlayground(playgroundLocation: String, playgroundSport: String){
//    Row(modifier = Modifier
//        .fillMaxWidth()
//        .padding(vertical = 10.dp)) {
//        Column(Modifier.weight(1f)) {
//            TextBasicIcon(playgroundLocation, Icons.Default.LocationOn)
//
//        }
//        Column(Modifier.weight(1f)) {
//            TextBasicIcon(playgroundSport, getIconSport(playgroundSport))
//
//        }
//    }
//}

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
