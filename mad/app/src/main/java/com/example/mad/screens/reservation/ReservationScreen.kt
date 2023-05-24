package com.example.mad.screens.reservation

import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.common.composable.FloatingButtonAdd
import com.example.mad.common.composable.IconButtonDelete
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextBasicIcon
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.common.composable.TopBarBasic
import com.example.mad.common.getIconSport
import com.example.mad.common.getMonth
import com.example.mad.common.getToday
import com.example.mad.model.Reservation
import com.example.mad.model.UserReservation
import com.example.mad.ui.theme.MadTheme
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

//TODO: fix error select date on previous month

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReservationScreen(
    navController: NavHostController,
    vm: MainViewModel
) {

    val orientation = LocalConfiguration.current.orientation

    //val userId = vm.currentUser.value.uid

    val reservations = remember {
        mutableStateOf<List<UserReservation>>(emptyList())
    }


//    fun update() {
//
//
//    }



    Scaffold(
        topBar = {
            TopBarBasic(
                id = R.string.topBarReservation,
            )
        },
//        floatingActionButton = { FloatingButtonAdd(::update) }
    ) {
        Column(
            Modifier
                .padding(it)
        ) {
            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {

                    Column(Modifier.weight(1.3f)) {
                        CalendarCard(
                            vm,
                            reservations
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        ReservationCard(reservations.value,navController,vm)
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
                                vm,
                                reservations
                            )
                        }
                        Column(Modifier.weight(1f)) {

                            ReservationCard(reservations.value,navController,vm)
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun CalendarCard(
    vm: MainViewModel,
    reservations: MutableState<List<UserReservation>>
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val userId = "66bvbnu9zPP3SzKD6W15ax8Ouhv1"

    val today = getToday()
    val dates = remember {
        mutableStateOf<List<String>>(emptyList())
    }
    vm.getAllUserReservationDates(userId).observe(
        LocalLifecycleOwner.current
    ) {
        dates.value = it.filterNotNull().toList()
    }

    Card(
        modifier = Modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(16.dp),
    ) {
        AndroidView(factory = { CustomCalendarView(it) }, update = {

            if (dates.value.isNotEmpty()) {
                val listDecorator: MutableList<DayDecorator> = mutableListOf()
                listDecorator.add(ColorDayDecorator(dates.value))
                it.decorators = listDecorator
                it.refreshCalendar(Calendar.getInstance(Locale.getDefault()))
            }

            it.setCalendarListener(object : CalendarListener {

                override fun onDateSelected(date: Date?) {
                    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    val dateText = date?.let { d -> df.format(d) }

                    val formattedDate = if (dateText.isNullOrEmpty()) today else dateText

                    vm.getAllUserReservationByDate(userId, formattedDate)
                        .observe(lifecycleOwner) { reservationList ->
                            reservations.value = reservationList.filterNotNull()
                        }

                }

                override fun onMonthChanged(date: Date?) {}
            })

        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationCard(
    reservations: List<UserReservation>,
    navController: NavHostController,
    vm: MainViewModel,
) {

    val today = getToday()

    val context = LocalContext.current

    fun onDelete() {
        //
    }

    if(reservations.isNotEmpty()){
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(reservations) {item ->
                Card(
                    colors = CardDefaults.cardColors(

                    ),
                    onClick = {
                        if(item.date>=today)
                            navController.navigate("editReservation/1")
                        else Toast.makeText(context,"You can't edit past reservation",Toast.LENGTH_SHORT).show()

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
                                TextBasicHeadLine(text = item.playground)
                            }

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 8.dp)
                                ) {
                                    TextBasicIcon(text = item.sport, icon = getIconSport(item.sport))
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 8.dp)
                                ) {
                                    TextBasicIcon(text = item.city, icon = Icons.Default.LocationOn)
                                }

                            }

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 8.dp)
                                ) {
                                    TextBasicIcon(text = item.date, icon = Icons.Default.CalendarMonth)
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 8.dp)
                                ) {
                                    TextBasicIcon(text = "${item.startTime}-${item.endTime}", icon = Icons.Default.AccessTime)
                                }

                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            IconButtonDelete(action = ::onDelete)
                        }
                    }
                }
            }
        }
    }
    else {
        Column(Modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
            Text(text = "No reservations",style=MaterialTheme.typography.bodyMedium,fontSize=24.sp)
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


