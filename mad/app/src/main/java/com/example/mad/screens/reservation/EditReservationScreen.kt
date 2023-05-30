package com.example.mad.screens.reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mad.MainViewModel
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.ListRadioButtonData
import com.example.mad.common.getIconSport
import com.example.mad.model.Reservation
import com.example.mad.screens.profile.TextIcon
import kotlinx.coroutines.delay


@Composable
fun FullDialogEditReservation(
    openDialog: MutableState<Boolean>,
    reservation: Reservation,
    vm: MainViewModel,
    changeUi:MutableState<Boolean>
//    timeSlot:List<String>
) {
    val descriptionText = "You can select another " +
            "time slot if are available and you can also rent " +
            "the equipment from the playground"

    val selectedTime = remember {
        mutableStateOf(0)
    }
    val checked = remember {
        mutableStateOf(false)
    }

    val timeSlot = vm.availableTimeSlot.observeAsState().value?.toList()?: emptyList()

    val loading = vm.loadingProgressBar.value

    fun editReservation() {
        if(timeSlot.isNotEmpty()){
            val time = timeSlot[selectedTime.value]
            val startTime = time.substringBefore("-")
            val endTime = time.substringAfter("-")
            val equipment = checked.value
            val data = hashMapOf<String, Any>(
                "endTime" to endTime,
                "startTime" to startTime,
                "equipment" to equipment
            )
            vm.updateReservation(reservation.id, data)
            vm.updateUserReservation(reservation.id, data)
        }
        changeUi.value=true
        openDialog.value = false
    }

    LaunchedEffect(key1 = null){
        vm.loadingProgressBar.value=true
        vm.getTimeSlotReservationByPlaygroundAndDate(reservation.date,reservation.address,reservation.city)
        delay(2000)
        vm.loadingProgressBar.value=false
    }

    Dialog(
        onDismissRequest = {
            openDialog.value = false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {

        Box(Modifier
            .fillMaxSize()){
            Column(
                Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RectangleShape
                    )
            ) {

                /*HEADER DIALOG*/
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        IconButton(onClick = {
                            openDialog.value = false
                        }, modifier = Modifier.fillMaxHeight()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "close dialog",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Column(
                        Modifier
                            .weight(5f)
                            .padding(horizontal = 10.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Edit your reservation",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(
                        Modifier
                            .weight(2f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                editReservation()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Text(text = "Save")
                        }
                    }
                }

                if(timeSlot.isNotEmpty()){
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        /*DESCRIPTION TEXT*/
                        Column(Modifier.padding(10.dp)) {
                            Text(
                                text = descriptionText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 18.sp
                            )
                        }
                        Divider(Modifier.padding(top=5.dp))

                        /*RESERVATION INFO*/
                        Column(Modifier.padding(vertical=30.dp)) {
                            /*PLAYGROUND*/
                            Column(Modifier.padding(start=10.dp,end=10.dp,bottom=10.dp)) {
                                Text(
                                    text = reservation.playground,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                )
                            }

                            /*ADDRESS, CITY*/
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, end = 15.dp)
                            ) {
                                TextIcon(
                                    text = reservation.address + " " + reservation.city,
                                    icon = Icons.Default.LocationOn
                                )
                            }

                            /*SPORT*/
                            Column(Modifier.padding(vertical = 20.dp)) {
                                TextIcon(text = reservation.sport, icon = getIconSport(reservation.sport))
                            }

                            /*DATE,TIME*/
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                            ) {
                                Column(Modifier.weight(1f)) {
                                    TextIcon(text = reservation.date, icon = Icons.Default.CalendarMonth)
                                }
                                Column(Modifier.weight(1f)) {
                                    TextIcon(
                                        text = reservation.startTime + "-" + reservation.endTime,
                                        icon = Icons.Default.AccessTime
                                    )
                                }
                            }
                        }

                        Divider(Modifier.padding(top = 20.dp, bottom = 10.dp))

                        /*TIME SLOT LIST*/

                        Text(
                            text = "Time Slot",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                        ListRadioButtonData(data = timeSlot, state = selectedTime,modifier=Modifier.height(150.dp))


                        Divider(Modifier.padding(vertical = 10.dp))

                        /*EQUIPMENT SWITCH*/
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = "Equipment",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                            Switch(
                                checked = checked.value,
                                onCheckedChange = { checked.value = it }
                            )

                        }
                    }
                }

                else {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No time slot available for this playground in this date, please select another " +
                                    "day for rent the playgroud",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                }
            }
            CircularProgressBar(isDisplayed = loading)
        }


    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewEditReservation2() {
//    val openDialog = remember {
//        mutableStateOf(true)
//    }
//    MadTheme {
//        FullDialogEditReservation(openDialog)
//    }
//}

//Landscape Preview

//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewEditReservation() {
//    val openDialog = remember {
//        mutableStateOf(true)
//    }
//    MadTheme {
//        FullDialogEditReservation(openDialog)
//    }
//}
