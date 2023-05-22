package com.example.mad.screens.reservation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.ButtonBasic
import com.example.mad.common.composable.CardReservation
import com.example.mad.common.composable.DialogList
import com.example.mad.common.composable.TextBasicBody
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TopBarComplete
import com.example.mad.model.Reservation
import com.example.mad.ui.theme.MadTheme


@Composable
fun EditReservationScreen(
    navController: NavHostController,
//    vm : MainViewModel,
//    reservationId : String?
) {
    val configuration = LocalConfiguration.current

    fun goToPreviousPage() {
        navController.navigate(BottomBarScreen.Reservation.route)
    }

    fun saveData() {
        //viewModel stuff
    }

    Scaffold(
        topBar = {
            TopBarComplete(
                id = R.string.topBarEditReservation,
                icon = Icons.Default.Check,
                backAction = ::goToPreviousPage,
                action = ::saveData
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitEditReservation(/*vm,reservationId,navController*/)
                }

                else -> {
//                    LandscapeEditReservation()
                }
            }
        }

    }
}

@Composable
fun PortraitEditReservation(
//    vm:MainViewModel,
//    reservationId: String?,
//    navController: NavHostController
) {

    val timeSlot = listOf<String>(
        "09:00-11:00",
        "11:00-13:00",
        "13:00-15:00",
        "15:00-17:00",
        "17:00-19:00",
    )
    val id = 0
    val (selectedTimeSlot, setSelectedTimeSlot) = remember {
        mutableStateOf("10:00-12:00")
    }
    val checkedState = remember { mutableStateOf(true) }

    val (isOpenDialog,openDialog) = remember {
        mutableStateOf(false)
    }


    if(isOpenDialog){
        DialogList(timeSlot , openDialog , setSelectedTimeSlot)
    }

    fun onClick() {
        openDialog(true)
    }

    val r = Reservation(
        address = "Corso Einaudi 75,10137",
        city = "Turin",
        date = "22/05/2023",
        email = "mariorossi@gmail.com",
        endTime = "13:00",
        equipment = false,
        fullName = "Mario Rossi",
        phone = "34567643243",
        playground = "Campo Admond",
        sport = "Soccer",
        startTime = "11:00"
    )


    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(3f)) {
            CardReservation(r)
        }
        Row(
            Modifier
                .weight(1f)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {

            Button(onClick = { onClick() }) {
                TextBasicHeadLine(text = stringResource(R.string.selectTimeSlot))
            }


        }

        if (selectedTimeSlot != "") {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
                TextBasicHeadLine(text = selectedTimeSlot)
            }
        }

        Row(
            Modifier
                .weight(1f)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextBasicHeadLine(text = stringResource(R.string.switchLabel))
                Spacer(Modifier.padding(horizontal = 10.dp))
                Switch(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it }
                )
            }
        }

    }
}


//Portrait Preview
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewEditReservation() {
//    MadTheme {
//        EditReservationScreen()
//    }
//}

//Landscape Preview

//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewEditReservation() {
//    MadTheme {
//        EditReservationScreen()
//    }
//}
