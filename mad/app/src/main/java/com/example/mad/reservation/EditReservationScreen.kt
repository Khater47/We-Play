package com.example.mad.reservation

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import com.example.mad.model.TimeSlot
import com.example.mad.utils.getIconSport

@Composable
fun EditReservationScreen(
    navController: NavHostController,
    vm : UserViewModel,
    reservationId : String?
) {

    val configuration = LocalConfiguration.current
    Log.d("Tag", reservationId?: "")
    Scaffold(
        topBar = { TopAppBarEditReservation() }
    ) {
        Box(Modifier.padding(it)) {
            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitEditReservation()
                }

                else -> {
                    LandscapeEditReservation()
                }
            }
        }
    }



}

@Composable
fun TopAppBarEditReservation(
//    navController: NavHostController
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
//                navController.navigate(BottomBarScreen.ProfileRating.route)
            }) {
                androidx.compose.material.Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {}

    )
}

@Composable
fun PortraitEditReservation() {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(2f)){
            ReservationPlaygroundContainer()

        }

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()){
            TimeSlotRow()
        }

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            ButtonEdit()
        }
    }
}

@Composable
fun LandscapeEditReservation() {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(1f)){
            ReservationPlaygroundContainer()

        }
        Column(Modifier.weight(1f)){
            ButtonEdit()
        }
    }

}


@Composable
fun ReservationPlaygroundContainer() {

    val playground = "Campo Admond"
    val sport = "Soccer"
    val location = "Torino"
    val date = "16/05/2023"
    val time = "10:00"


    androidx.compose.material3.Card(
        Modifier
            .fillMaxWidth())
    {
        Column(Modifier.fillMaxWidth()) {
            Text(text=playground,fontSize=22.sp,
                modifier=Modifier.padding(horizontal=10.dp,vertical=20.dp))

//                    Image(painter = painterResource(id = getIconPlayground(sport)),
//                        contentDescription = "Image Playground",
//                        modifier= Modifier
//                            .height(150.dp)
//                            .padding(vertical = 10.dp))


            ReservationInfo(sport,location, date, time)
        }
    }



}

@Composable
fun ButtonEdit(){

    Button(onClick = {

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
fun TimeSlotRow(){

    val timeSlot = listOf(
        TimeSlot(1,"09:00"),
        TimeSlot(1,"10:00"),
        TimeSlot(1,"11:00"),
        TimeSlot(1,"12:00"),
        TimeSlot(1,"13:00"),
    )

    LazyRow{
        items(timeSlot){ item ->
            Card(
                elevation=10.dp,
                modifier=Modifier.padding(10.dp)
            ) {
                Column {
                    Text(text=item.time,fontSize=18.sp,modifier=Modifier.padding(10.dp))
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





/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ReservationScreen()
    }
}
*/
