package com.example.mad.reservation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import com.example.mad.activity.BottomBarScreen
import com.example.mad.model.Reservation
import com.example.mad.model.TimeSlot
import com.example.mad.utils.getIconSport

@Composable
fun EditReservationScreen(
    navController: NavHostController,
    vm : UserViewModel,
    reservationId : String?
) {

    val configuration = LocalConfiguration.current



    Scaffold(
        topBar = { TopAppBarEditReservation(navController) }
    ) {
        Box(Modifier.padding(it)) {
            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitEditReservation(vm,reservationId,navController)
                }

                else -> {
//                    LandscapeEditReservation()
                }
            }
        }
    }



}


@Composable
fun PortraitEditReservation(vm:UserViewModel,reservationId: String?,navController: NavHostController) {

    val timeSlot = vm.timeSlot().observeAsState().value?: emptyList()

    val id = reservationId?.toInt()?:0

    val reservation = vm.getReservationById(id).observeAsState().value

    val (selectedTimeSlot,setSelectedTimeSlot) = remember {
        mutableStateOf("")
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if(reservation!=null){

            Column(Modifier.weight(2f)){
                ReservationPlaygroundContainer(reservation,vm)

            }
        }

        if(selectedTimeSlot.isNotEmpty()){
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text=selectedTimeSlot, fontSize = 20.sp)
            }
        }

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()){
            TimeSlotRow(timeSlot,setSelectedTimeSlot)
        }

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            ButtonEdit(reservation,selectedTimeSlot,vm, navController)
        }
    }
}



@Composable
fun ReservationPlaygroundContainer(reservation: Reservation,vm: UserViewModel) {

    val p = vm.getPlaygroundById(reservation.idPlayground).observeAsState().value

    if(p!=null){
        val playground = p.playground
        val sport = p.sport
        val location = p.location
        val date = reservation.date
        val time = reservation.startTime+"-"+reservation.endTime


        androidx.compose.material3.Card(
            Modifier
                .fillMaxWidth())
        {
            Column(Modifier.fillMaxWidth()) {
                Text(text=playground,fontSize=22.sp,
                    modifier=Modifier.padding(horizontal=10.dp,vertical=20.dp))



                ReservationInfo(sport,location, date, time)
            }
        }
    }





}

@Composable
fun ButtonEdit(reservation:Reservation?,selectedTimeSlot:String,vm:UserViewModel,navController: NavHostController){

    Button(onClick = {
             if(reservation!=null && selectedTimeSlot!=""){
                 val startTime = selectedTimeSlot.substringBefore("-")
                 val endTime = selectedTimeSlot.substringAfter("-")
                        val r = Reservation(
                            id=reservation.id,
                            date=reservation.date,
                            equipment=reservation.equipment,
                            idPlayground=reservation.idPlayground,
                            startTime,
                            endTime,
                            idProfile=reservation.idProfile
                        )
                 vm.insertReservation(r)
                 navController.navigate(BottomBarScreen.Reservation.route)
             }

    },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF32CD32))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Icon(imageVector = Icons.Default.Check, contentDescription = "Edit Icon",
                modifier=Modifier.size(20.dp)
            )
            Text(text="Edit",fontSize=20.sp,modifier=Modifier.padding(horizontal = 10.dp))
        }
    }

}



@Composable
fun TimeSlotRow(timeSlot:List<TimeSlot>,setSelectedTimeSlot:(String)->Unit){

    LazyRow{
        items(timeSlot){ item ->
            val time = item.startTime+"-"+item.endTime
            Card(
                elevation=10.dp,
                modifier= Modifier
                    .padding(10.dp)
                    .clickable {
                        setSelectedTimeSlot(time)
                    }
            ) {
                Column {
                    Text(text= time,fontSize=18.sp,modifier=Modifier.padding(10.dp))
                }
            }
        }
    }
}

@Composable
fun ReservationInfo(
    sport: String,
    location: String,
    date: String,
    time: String
) {

    //sport & location
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row{
                Icon(imageVector = getIconSport(sport), contentDescription = "Icon Sport")
                Text(text=sport,fontSize=18.sp,modifier=Modifier.padding(horizontal=10.dp))
            }
        }
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {

            Row{
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Icon Location")
                Text(text=location,fontSize=18.sp,modifier=Modifier.padding(horizontal=10.dp))
            }
        }

    }

    //date & time
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row{
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Icon Date")
                Text(text=date,fontSize=18.sp,modifier=Modifier.padding(horizontal=10.dp))
            }

        }
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row{
                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Icon Time")
                Text(text=time,fontSize=18.sp,modifier=Modifier.padding(horizontal=10.dp))
            }
        }

    }

}

//@Composable
//fun LandscapeEditReservation() {
//
//    Column(
//        Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Column(Modifier.weight(1f)){
//            ReservationPlaygroundContainer()
//
//        }
//        Column(Modifier.weight(1f)){
//            ButtonEdit()
//        }
//    }
//
//}
//
//

@Composable
fun TopAppBarEditReservation(
    navController: NavHostController
) {


    TopAppBar(
        title = {
            Text(
                text = "Edit Reservation",
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(BottomBarScreen.Reservation.route)
            }) {
                androidx.compose.material.Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {}

    )
}


/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ReservationScreen()
    }
}
*/
