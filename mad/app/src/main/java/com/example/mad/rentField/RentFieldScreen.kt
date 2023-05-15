package com.example.mad.rentField


import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad.activity.BottomBarScreen
import com.example.mad.model.Playgrounds
import com.example.mad.profileRating.TopAppBarRating
import com.example.mad.ui.theme.MadTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.mad.UserViewModel
import com.example.mad.model.Reservation
import com.example.mad.model.TimeSlot
import com.example.mad.utils.getIconPlayground
import com.example.mad.utils.getIconSport
import java.util.Locale


@Composable
fun RentFieldScreen() {


    Scaffold(
        topBar = { TopAppBarRentField() }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Container()
        }
    }


}

fun formatSetTime(
    startTime: String,
    endTime: String,
    setStartTime: (String) -> Unit,
    setEndTime: (String) -> Unit
): String {

    return if (startTime.isNotEmpty() && endTime.isNotEmpty()) {

        if (startTime > endTime) {
            val tmp = startTime
            setStartTime(endTime)
            setEndTime(tmp)
        }
        "$startTime-$endTime"
    } else if (startTime.isNotEmpty() && endTime.isEmpty())
        startTime
    else if (startTime.isEmpty() && endTime.isNotEmpty())
        endTime
    else ""

}

fun formatDate(day: Int, month: Int, year: Int): String {


    val d = if (day < 10) "0${day}" else "$day"
    val m = if (month < 9) "0${month + 1}" else "${month + 1}"

    return "$d/$m/$year"
}

@Composable
fun Container() {

    val calendar = Calendar.getInstance(Locale.ITALY)
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val today = formatDate(day, month, year)

    val (openDateDialog, setOpenDateDialog) = remember { mutableStateOf(false) }
    val (date, setDate) = remember { mutableStateOf(today) }

    val clearButton = remember {
        mutableStateOf(false)
    }

    val list = listOf(
        Playgrounds(1, "soccer", "Campo Admond", "Torino"),
        Playgrounds(1, "basketball", "Campo Groot", "Roma")
    )



    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

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
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    //DateDialog
                    setOpenDateDialog(true)

                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "dateDialog"
                        )
                        Text("Date", Modifier.padding(horizontal = 5.dp), fontSize = 20.sp)

                    }
                }
            }
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        //Clear Button
                        clearButton.value = true
                        setDate("")
                        setOpenDateDialog(false)
                    },

                    ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "dateDialog")
                        Text("Clear", Modifier.padding(horizontal = 5.dp), fontSize = 20.sp)

                    }
                }
            }
        }

        LazyColumn() {
            items(list) { item ->
                CardAvailablePlayground(date, item,clearButton.value)

            }
        }


    }

    if (openDateDialog) {
        OpenDateDialog(setDate, setOpenDateDialog)
    }
}

@Composable
fun CardAvailablePlayground(
    date: String,
    item:Playgrounds,
    clearButton:Boolean
) {

    val (startTime, setStartTime) = remember { mutableStateOf("") }
    val (endTime, setEndTime) = remember { mutableStateOf("") }


    val timeSlotFake = listOf(
        TimeSlot(1, "09:00"),
        TimeSlot(1, "09:30"),
        TimeSlot(1, "10:00"),
        TimeSlot(1, "10:30"),
        TimeSlot(1, "11:00"),
        TimeSlot(1, "11:30"),
        TimeSlot(1, "12:00"),
    )

    if(clearButton){
        setStartTime("")
        setEndTime("")
    }

    DateTimeRow(
        date = date,
        time = formatSetTime(startTime, endTime, setStartTime, setEndTime)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = 16.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Text(text = item.playground, modifier = Modifier.padding(10.dp), fontSize = 20.sp)

            Image(
                painter = painterResource(id = getIconPlayground(item.sport)),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "IconLocation",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = item.location,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                }
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = getIconSport(item.sport),
                            contentDescription = "IconLocation",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = item.sport,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
//                            val r = Reservation(
//                                0,
//                                date,
//                                equipment = 1,
//                                idPlayground = 1,
//                                idStartTimeSlot = 1,
//                                idEndTimeSlot = 2,
//                                2
//                            )
//                            vm.insertReservation(r)
                }) {
                    Text("Book Field", fontSize = 20.sp)
                }
            }

        }


    }


    TimeSlotRow(timeSlotFake, startTime, endTime, setStartTime, setEndTime)

}

@Composable
fun DateTimeRow(date: String, time: String) {


    if (date != "" || time != "") {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            if (date != "") {
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "calendarDate",
                            Modifier.padding(horizontal = 5.dp)
                        )
                        Text(text = date, fontSize = 18.sp)
                    }
                }
            }

            if (time != "") {
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "calendarDate",
                            Modifier.padding(horizontal = 5.dp)
                        )
                        Text(text = time, fontSize = 18.sp)
                    }
                }
            }


        }
    }

}


@Composable
fun OpenDateDialog(setDate: (String) -> Unit, setOpenDateDialog: (Boolean) -> Unit) {

    val calendar = Calendar.getInstance()

    val y = calendar.get(Calendar.YEAR)
    val m = calendar.get(Calendar.MONTH)
    val d = calendar.get(Calendar.DAY_OF_MONTH)

    val context = LocalContext.current

    val datePickerDialog =
        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, day ->

            val monthOfYear = if (month < 9) "0${month + 1}" else month.toString()
            val dayOfMonth = if (day < 10) "0${day}" else day.toString()


            setDate("$dayOfMonth/$monthOfYear/$year")
            setOpenDateDialog(false)

//            date = "$dayOfMonth/$monthOfYear/$year"
//
//            if(sport.isNotEmpty() && date.isNotEmpty() && time.isEmpty()){
//                vm.getAvailablePlaygroundsByDateAndSportAndPlayground(sport,date,playground).observe(this, Observer {
//                    adapter.setAvailablePlayground(it)
//                })
//            }
//
//            else if(sport.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()){
//                vm.getAvailablePlaygroundsByAllFilter(sport,time,date,playground).observe(this, Observer {
//                    adapter.setAvailablePlayground(it)
//                })
//            }


        }, y, m, d)

    datePickerDialog.show()


}

@Composable
fun TimeSlotRow(
    timeSlot: List<TimeSlot>,
    startTime: String,
    endTime: String,
    setStartTime: (String) -> Unit,
    setEndTime: (String) -> Unit
) {


    fun handleTime(
        time: String,
        startTime: String,
        endTime: String,
        setStartTime: (String) -> Unit,
        setEndTime: (String) -> Unit
    ) {

        if (startTime.isEmpty() && endTime.isEmpty()) {
            //set start time
            setStartTime(time)
        } else if (startTime.isNotEmpty() && endTime.isEmpty()) {

            //set end time
            if (time != startTime)
                setEndTime(time)
            //unset start time
            else
                setStartTime("")

        } else if (startTime.isEmpty() && endTime.isNotEmpty()) {

            //set start time
            if (time != endTime)
                setStartTime(time)
            //unset end time
            else
                setEndTime("")

        } else if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
            //unset start time
            if (time == startTime) {
                setStartTime("")
            }
            //unset end time
            else if (time == endTime) {
                setEndTime("")
            }
            // newTime>startTime => setEndTime(newTime)
            else if (time > startTime) {
                setEndTime(time)
            }
            // newTime<startTime => setStartTime(newTime)
            else if (time < startTime) {
                setStartTime(time)
            }


        }


    }

    LazyRow {
        items(timeSlot, itemContent = { item ->

            Card(
                elevation = 10.dp, modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        handleTime(item.time, startTime, endTime, setStartTime, setEndTime)
                    },
                backgroundColor = if (startTime == item.time || endTime == item.time)
                    Color(0xFF5891F7)
                else Color.White
            ) {
                Column(Modifier.padding(10.dp)) {
                    Text(item.time, fontSize = 18.sp)
                }
            }
        })
    }
}

@Composable
fun TopAppBarRentField(
//    navController:NavHostController
) {


    TopAppBar(
        title = {
            Text(
                text = "Rent Field",
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        RentFieldScreen()
    }
}