package com.example.mad.rentField


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.icu.util.Calendar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import com.example.mad.activity.BottomBarScreen
import com.example.mad.model.AvailablePlayground
import com.example.mad.model.Reservation
import com.example.mad.utils.getIconPlayground
import com.example.mad.utils.getIconSport
import java.util.Locale

fun formatDate(day:Int,month:Int,year:Int):String{
    val d = if(day<9) "09" else day.toString()
    val m = if(month<9) "0${month+1}" else month.toString()
    val y = year.toString()
    return "$d/$m/$y"
}


@Composable
fun RentFieldScreen(navController: NavHostController, vm: UserViewModel) {


    val calendar = Calendar.getInstance(Locale.ITALY)
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val today = formatDate(day, month, year)

    val (openDateDialog, setOpenDateDialog) = remember { mutableStateOf(false) }
    val (openTimeDialog, setOpenTimeDialog) = remember { mutableStateOf(false) }

    val (date, setDate) = remember { mutableStateOf(today) }
    val (time, setTime) = remember { mutableStateOf("") }


    val playgrounds = if (date.isEmpty() && time.isEmpty()) {
        vm.getAvailablePlaygroundOrdered().observeAsState().value ?: emptyList()

    } else if (date.isNotEmpty() && time.isEmpty()) {
        vm.getAvailablePlaygroundByDate(date).observeAsState().value ?: emptyList()

    } else if (date.isEmpty() && time.isNotEmpty()) {
        vm.getAvailablePlaygroundByTime(time).observeAsState().value ?: emptyList()

    } else {
        vm.getAvailablePlaygroundsByAllFilter(time, date).observeAsState().value ?: emptyList()

    }


    Scaffold(
        topBar = { TopAppBarRentField(navController) }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            RentFieldScreenPage(
                openDateDialog,
                setOpenDateDialog,
                openTimeDialog,
                setOpenTimeDialog,
                setDate,
                playgrounds,
                setTime,
                vm,
                date,
                time
            )

        }
    }


}

@Composable
fun RentFieldScreenPage(
    openDateDialog: Boolean,
    setOpenDateDialog: (Boolean) -> Unit,
    openTimeDialog: Boolean,
    setOpenTimeDialog: (Boolean) -> Unit,
    setDate: (String) -> Unit,
    availablePlaygrounds: List<AvailablePlayground>,
    setTime: (String) -> Unit,
    vm: UserViewModel,
    date:String,
    time:String
) {

    val orientation = LocalConfiguration.current.orientation


    Column(Modifier.padding(horizontal = 10.dp)) {

        ButtonGroup(
            setOpenTimeDialog,
            setOpenDateDialog,
            setTime,
            setDate,
        )


        Row(modifier = Modifier.fillMaxWidth()) {

            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Selected Date",
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = if(date=="") "dd/mm/yyyy" else  date,
                        modifier = Modifier.padding(10.dp)
                    )
                }

            }
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Selected Time",
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = if(time=="") "--:--" else time,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
        LazyColumn {
            items(availablePlaygrounds) { item ->
                when (orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        CardAvailablePlayground(item, vm)
                    }

                    else -> {
                        CardAvailablePlaygroundLandscape(item, vm)
                    }
                }

            }
        }
    }
    if (openDateDialog) {
        OpenDateDialog(setDate, setOpenDateDialog)
    }
    if (openTimeDialog) {
        OpenTimeDialog(setOpenTimeDialog, setTime)
    }

}


@Composable
fun ButtonGroup(
    setOpenTimeDialog: (Boolean) -> Unit,
    setOpenDateDialog: (Boolean) -> Unit,
    setTime: (String) -> Unit,
    setDate: (String) -> Unit,
) {

    val orientation = LocalConfiguration.current.orientation

    var verticalAlignment: Alignment.Vertical = Alignment.Top
    var horizontalAlignment: Arrangement.Horizontal = Arrangement.SpaceBetween
    var modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp)

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        verticalAlignment = Alignment.CenterVertically
        horizontalAlignment = Arrangement.SpaceEvenly
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    }

    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalAlignment
    ) {

        Button(onClick = {
            //DateDialog
            setOpenDateDialog(true)

        }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "dateDialog"
                )
                Text("Date", Modifier.padding(horizontal = 5.dp), fontSize = 20.sp)

            }
        }


        Button(onClick = {
            //TimeDialog
            setOpenTimeDialog(true)

        }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "timeDialog"
                )
                Text("Time", Modifier.padding(horizontal = 5.dp), fontSize = 20.sp)

            }
        }


        Button(
            onClick = {
                //Clear Button
                setDate("")
                setTime("")
                setOpenDateDialog(false)
                setOpenDateDialog(false)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)

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

@Composable
fun CardAvailablePlayground(
    availablePlaygrounds: AvailablePlayground,
    vm: UserViewModel,
) {

    val checkedState = remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = 16.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Text(
                text = availablePlaygrounds.playground,
                modifier = Modifier.padding(10.dp),
                fontSize = 20.sp
            )

            Image(
                painter = painterResource(id = getIconPlayground(availablePlaygrounds.sport)),
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
                            text = availablePlaygrounds.location,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                }
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = getIconSport(availablePlaygrounds.sport),
                            contentDescription = "IconLocation",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = availablePlaygrounds.sport,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "IconDate",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = availablePlaygrounds.date,
                            modifier = Modifier.padding(10.dp),

                        )
                    }

                }
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "IconTime",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = availablePlaygrounds.startTime + "-" + availablePlaygrounds.endTime,
                            modifier = Modifier.padding(10.dp),

                        )
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    val r = Reservation(
                        0,
                        availablePlaygrounds.date,
                        equipment = if(checkedState.value) 1 else 0,
                        availablePlaygrounds.id,
                        availablePlaygrounds.startTime,
                        availablePlaygrounds.endTime,
                        2,
                    )
                    vm.insertReservation(r)
                    vm.deleteAvailablePlayground(availablePlaygrounds)

                }) {
                    Text("Book Field", fontSize = 20.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = {
                            checkedState.value = it
                        }
                    )
                    Text(
                        "Equipment",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
            }

        }


    }


}

@Composable
fun CardAvailablePlaygroundLandscape(
    availablePlaygrounds: AvailablePlayground,
    vm: UserViewModel,

) {
    val checkedState = remember {
        mutableStateOf(false)
    }

    Row(Modifier.fillMaxWidth()) {
        Column(Modifier.weight(1f)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
            ) {
                Column {
                    Text(
                        text = availablePlaygrounds.playground,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 20.sp
                    )

                    Image(
                        painter = painterResource(id = getIconPlayground(availablePlaygrounds.sport)),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                    )

                }

            }
        }

        Column(Modifier.weight(1f)) {

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "IconLocation",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = availablePlaygrounds.location,
                            modifier = Modifier.padding(10.dp),
                            fontSize = 20.sp
                        )
                    }

                }
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = getIconSport(availablePlaygrounds.sport),
                            contentDescription = "IconSport",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = availablePlaygrounds.sport,
                            modifier = Modifier.padding(10.dp),
                            fontSize = 20.sp
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "IconDate",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = availablePlaygrounds.date,
                            modifier = Modifier.padding(10.dp),
                            fontSize = 20.sp
                        )
                    }

                }
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "IconTime",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = availablePlaygrounds.startTime + "-" + availablePlaygrounds.endTime,
                            modifier = Modifier.padding(10.dp),
                            fontSize = 20.sp
                        )
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    val r = Reservation(
                        0,
                        availablePlaygrounds.date,
                        equipment = if(checkedState.value) 1 else 0,
                        availablePlaygrounds.id,
                        availablePlaygrounds.startTime,
                        availablePlaygrounds.endTime,
                        2,
                    )
                    vm.insertReservation(r)
                    vm.deleteAvailablePlayground(availablePlaygrounds)
                }) {
                    Text("Book Field", fontSize = 20.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = {
                            checkedState.value = it
                        }
                    )
                    Text(
                        "Equipment",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
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
        DatePickerDialog(context, { _, year, month, day ->

            val monthOfYear = if (month < 9) "0${month + 1}" else month.toString()
            val dayOfMonth = if (day < 10) "0${day}" else day.toString()

            val newDate = "$dayOfMonth/$monthOfYear/$year"
            setDate(newDate)
            setOpenDateDialog(false)
        }, y, m, d)

    datePickerDialog.show()


}

@Composable
fun OpenTimeDialog(setOpenTimeDialog: (Boolean) -> Unit, setTime: (String) -> Unit) {
    val calendar = Calendar.getInstance()

    val context = LocalContext.current

    val h = calendar.get(Calendar.HOUR)
    val m = calendar.get(Calendar.MINUTE)
    val is24HourView = true


    val timePickerDialog = TimePickerDialog(context, { _, hour, minute ->

        val hourFormatted = if (hour < 10) "0$hour" else hour.toString()
        val minuteFormatted = if (minute < 10) "0$minute" else minute.toString()

        val time = "$hourFormatted:$minuteFormatted"
        setTime(time)
        setOpenTimeDialog(false)


    }, h, m, is24HourView)

    timePickerDialog.show()

}


@Composable
fun TopAppBarRentField(
    navController: NavHostController
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
                navController.navigate(BottomBarScreen.Home.route)
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {},
        elevation = 10.dp

    )
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        RentFieldScreen()
//    }
//}

