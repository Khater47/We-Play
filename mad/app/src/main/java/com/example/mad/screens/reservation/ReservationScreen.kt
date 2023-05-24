package com.example.mad.screens.reservation

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.common.composable.IconButtonDelete
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextBasicIcon
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.common.composable.TopBarBasic
import com.example.mad.common.getIconSport
import com.example.mad.common.getToday
import com.example.mad.ui.theme.MadTheme
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReservationScreen(
    navController: NavHostController,
//    vm: MainViewModel
) {
    val today = getToday()
    val orientation = LocalConfiguration.current.orientation

    val dateState = remember {
        mutableStateOf(
            today
        )
    }


    val dates = listOf<String>(
        "25/05/2023",
        "11/05/2023",
        "07/05/2023",
        "14/05/2023",
        "10/05/2023",
    )

    Scaffold(
        topBar = {
            TopBarBasic(
                id = R.string.topBarReservation,
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
        ) {
            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {

                    Column(Modifier.weight(1f)) {
                        CalendarCard(
                            dates,
                            today,
                            dateState
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        ReservationCard(navController)
                    }
                }

                else -> {
                    Row(
                        Modifier
                            .fillMaxHeight()
                    ) {
                        Column(
                            Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {

                            CalendarCard(
                                dates,
                                today,
                                dateState
                            )
                        }
                        Column(Modifier.weight(1f)) {

                            ReservationCard(navController)
                        }
                    }
                }
            }
        }
    }

}



@Composable
fun CalendarCard(
    dates: List<String>,
    today: String,
    dateState: MutableState<String>
) {
    Card(
        modifier = Modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(16.dp),
    ) {
        AndroidView(factory = { CustomCalendarView(it) }, update = {

            if (dates.isNotEmpty()) {
                val listDecorator: MutableList<DayDecorator> = mutableListOf()
                listDecorator.add(ColorDayDecorator(dates))
                it.decorators = listDecorator
                it.refreshCalendar(Calendar.getInstance(Locale.getDefault()))
            }

            it.setCalendarListener(object : CalendarListener {

                override fun onDateSelected(date: Date?) {
                    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    val dateText = date?.let { d -> df.format(d) }

                    val formattedDate = if (dateText.isNullOrEmpty()) today else dateText

                    dateState.value = formattedDate

                    //Invoke vm.getReservationByDate
                }

                override fun onMonthChanged(date: Date?) {}
            })

        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationCard(
//    reservationList: List<Reservation>,
    navController: NavHostController,
//    vm: UserViewModel,
) {
    val playground = "Campo Admond"
    val sport = "Soccer"
    val location = "Turin"
    val date = "24/05/2023"
    val time = "10:00-12:00"

    fun onClick() {
        //
    }


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(5) {
            Card(
                colors = CardDefaults.cardColors(

                ),
                onClick = {
                navController.navigate("editReservation/1")
                },
                elevation = CardDefaults.cardElevation(),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Row {
                    Column(modifier = Modifier.weight(4.3f)) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            TextBasicHeadLine(text = playground)
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                            ) {
                                TextBasicIcon(text = sport, icon = getIconSport(sport))
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                            ) {
                                TextBasicIcon(text = location, icon = Icons.Default.LocationOn)
                            }

                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                            ) {
                                TextBasicIcon(text = date, icon = Icons.Default.CalendarMonth)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                            ) {
                                TextBasicIcon(text = time, icon = Icons.Default.AccessTime)
                            }

                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        IconButtonDelete(action = ::onClick)
                    }
                }
            }
        }
    }
}


class ColorDayDecorator(private val dates: List<String>) : DayDecorator {
    override fun decorate(dayView: DayView) {
        val date = dayView.date
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = date?.let { df.format(it) }

        if (dates.contains(formattedDate)) {
            val color: Int = android.graphics.Color.parseColor("#a9afb9")
            dayView.setBackgroundColor(color)
        }

    }
}


//Portrait Preview

//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        ReservationScreen()
//    }
//}


//Landscape Preview

//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        ReservationScreen()
//    }
//}


