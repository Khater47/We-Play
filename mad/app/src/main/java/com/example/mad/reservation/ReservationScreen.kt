package com.example.mad.reservation


import android.os.Build
import android.util.Log
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.material.Card
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import java.time.LocalDate
import androidx.compose.runtime.livedata.observeAsState
import com.example.mad.model.Reservation
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReservationScreen(navController: NavHostController, vm: UserViewModel) {

    val reservationList = vm.reservations.observeAsState().value ?: emptyList()
    val playgroundList = vm.playgrounds.observeAsState().value ?: emptyList()

    val reservationPlayground = mutableListOf<ReservationPlayground>()

    reservationList.forEach { r ->
        val p = playgroundList.firstOrNull { r.idPlayground == it.id }
        if (p != null) {
            reservationPlayground.add(
                ReservationPlayground(r.id, r.date, p.sport, p.playground, p.location)
            )
        }
    }

    Log.d("tag", reservationList.size.toString())

    //reservationList.addAll(vm.reservations)

    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)
    val today = formatDate(day,month,year)

    var date by remember {
        mutableStateOf(
            today
        )
    }

    Scaffold(topBar = {
        TopAppBarReservation()

    }) { it ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            AndroidView(factory = { CalendarView(it) }, update = {

                it.setOnDateChangeListener { _, year, month, day ->
                    date = formatDate(day, month, year)
                }
            })
            Text(text = date)

            ReservationCard(reservationPlayground.filter { (it.date == date) }, navController)


        }
    }

}

data class ReservationPlayground(
    val id: Int,
    val date: String,
    val sport: String,
    val playground: String,
    val location: String
)

fun formatDate(day: Int, month: Int, year: Int): String {
    val m = if (month < 9) "0${month + 1}" else "${month + 1}"
    val d = if (day < 10) "0${day}" else "$day"
    return "$d/$m/$year"
}

@Composable
fun ReservationCard(
    reservationList: List<ReservationPlayground>,
    navController: NavHostController
) {


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(reservationList, itemContent = { item ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        navController.navigate("editReservation/${item.id}")
                    },
                elevation = 10.dp,
                shape = RoundedCornerShape(16.dp),
            ) {
                Row {
                    Column(modifier = Modifier.weight(3f)) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = item.date,
                                style = MaterialTheme.typography.bodyLarge,
                            )

                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)) {
                                Text(
                                    text = item.playground,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }


                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)) {
                                Text(text = item.sport, style = MaterialTheme.typography.bodyLarge)
                            }

                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)) {
                                Text(
                                    text = item.location,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                        }

                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        IconButton(
                            onClick = {
                                      //DELETE RESERVATION
//                                      val r = Reservation(
//
//                                      )

                            },

                            ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "",
                                tint = Color.Red
                            )
                        }
                    }
                }


            }
        })
    }

}


@Composable
fun TopAppBarReservation(
    //navController: NavHostController
) {


    TopAppBar(
        title = {
            androidx.compose.material.Text(
                text = "Reservation",
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        },
        navigationIcon = {
            IconButton(onClick = {
//                navController.navigate(BottomBarScreen.Home.route)
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {},
        elevation = 10.dp

    )
}


/*@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ReservationScreen()
    }
}*/
