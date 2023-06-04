package com.example.mad.screens.reservation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mad.DELAY
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.DeleteDialog
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextBasicIcon
import com.example.mad.common.composable.TopBarBasic
import com.example.mad.common.getIconSport
import com.example.mad.common.getToday
import com.example.mad.model.Reservation
import com.example.mad.model.UserReservation
import com.example.mad.model.toReservation
import com.example.mad.ui.theme.confirmation
import com.example.mad.ui.theme.md_theme_light_onSecondary
import com.example.mad.ui.theme.md_theme_light_secondary
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReservationScreen(
    vm: MainViewModel
) {
    val loading = vm.loadingProgressBar.value

    val orientation = LocalConfiguration.current.orientation

    val changeUi = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = changeUi.value) {
        if (changeUi.value) {
            vm.loadingProgressBar.value = true
            val today = getToday()
            val month = Calendar.getInstance().get(Calendar.MONTH) + 1
            val formattedMonth = if (month < 10) "0$month" else month.toString()
            delay(DELAY)
            vm.getDatesReservationByMonth(formattedMonth)
            vm.getAllUserReservationByDate(today)
            vm.loadingProgressBar.value = false
            changeUi.value = false
        }

    }

    Scaffold(
        topBar = {
            TopBarBasic(
                id = R.string.topBarReservation,
            )
        },
    ) {
        Box(
            Modifier.padding(it)
        ) {
            Column {
                when (orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {

                        Column(Modifier.weight(1.3f)) {
                            CalendarCard(vm)

                        }
                        Column(Modifier.weight(1f)) {
                            ReservationCard(/*navController,*/ vm, changeUi)
                        }
                    }

                    else -> {
                        Row(
                            Modifier.fillMaxHeight()
                        ) {
                            Column(
                                Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                            ) {

                                CalendarCard(vm)

                            }
                            Column(Modifier.weight(1f)) {

                                ReservationCard(vm, changeUi)
                            }
                        }
                    }
                }
            }
            CircularProgressBar(isDisplayed = loading)
        }
    }

}


@Composable
fun CalendarCard(
    vm: MainViewModel,
) {

    val dates = vm.dates.observeAsState().value ?: emptyList()

    val calendar = Calendar.getInstance()

    val year = remember {
        mutableStateOf(calendar.get(Calendar.YEAR))
    }
    val month = remember {
        mutableStateOf(calendar.get(Calendar.MONTH))
    }

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
                val c = Calendar.getInstance()
                c.set(Calendar.YEAR, year.value)
                c.set(Calendar.MONTH, month.value)
                it.refreshCalendar(c)
            }

            it.setCalendarListener(object : CalendarListener {

                override fun onDateSelected(date: Date?) {

                    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    val dateText = date?.let { d -> df.format(d) }

                    if (!dateText.isNullOrEmpty()) {
                        val y = dateText.substringAfterLast("/").toInt()
                        val m = dateText.substringAfter("/").substringBeforeLast("/")
                        val formattedMonth = if (m.startsWith("0")) m.replace("0", "").toInt()
                        else m.toInt()

                        year.value = y
                        month.value = formattedMonth - 1

                        vm.getAllUserReservationByDate(dateText)
                    }


                }

                override fun onMonthChanged(date: Date?) {
                    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    val dateText = date?.let { d -> df.format(d) }

                    if (!dateText.isNullOrEmpty()) {
                        val y = dateText.substringAfterLast("/").toInt()
                        val m = dateText.substringAfter("/").substringBeforeLast("/")
                        val formattedMonth = if (m.startsWith("0")) m.replace("0", "").toInt()
                        else m.toInt()
                        year.value = y
                        month.value = formattedMonth - 1

                        vm.getDatesReservationByMonth(m)
                    }

                }
            })

        })
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ReservationCard(
    vm: MainViewModel,
    changeUi: MutableState<Boolean>
) {

    val openDialog = remember {
        mutableStateOf(false)
    }

    val reservations = vm.userReservation.observeAsState().value ?: emptyList()

    val id = remember {
        mutableStateOf("")
    }


    val openDialogDelete = remember {
        mutableStateOf(false)
    }


    val selectedReservation = remember {
        mutableStateOf(
            Reservation(
                "",
                "",
                "",
                "",
                "",
                false,
                "",
                "",
                "",
                ""
            )
        )
    }

    val today = getToday()

    val context = LocalContext.current

    if (openDialog.value) {
        FullDialogEditReservation(
            openDialog,
            selectedReservation.value,
            vm,
            changeUi
        )
    }



    fun actionDialog() {
        if (selectedReservation.value.id != "") {
            vm.deleteReservation(id.value)
            vm.deleteUserReservation(id.value)
            vm.getAllUserReservationByDate(selectedReservation.value.date)
            openDialogDelete.value=false
            changeUi.value=true
        }
    }

    if (openDialogDelete.value) {
        DeleteDialog(
            "Delete Reservation",
            text = "are you sure to delete this reservation",
            openDialogDelete,
            ::actionDialog
        )
    }

    fun editReservation(item: UserReservation) {
        val email = vm.currentUser.value?.email ?: ""
        if (item.date >= today) {

            selectedReservation.value = Reservation(
                item.address,
                item.city,
                item.date,
                email,
                item.endTime,
                item.equipment,
                item.id,
                item.playground,
                item.sport,
                item.startTime
            )
            openDialog.value = true
        } else Toast.makeText(
            context, "You can't edit past reservation", Toast.LENGTH_SHORT
        ).show()
    }

    Box() {
        if (reservations.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                items(reservations) { item ->
                    Card(
                        colors = CardDefaults.cardColors(),
                        elevation = CardDefaults.cardElevation(),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
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
                                        TextBasicIcon(
                                            text = item.sport, icon = getIconSport(item.sport)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 8.dp)
                                    ) {
                                        TextBasicIcon(
                                            text = item.city,
                                            icon = Icons.Default.LocationOn
                                        )
                                    }

                                }

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 8.dp)
                                    ) {
                                        TextBasicIcon(
                                            text = item.date, icon = Icons.Default.CalendarMonth
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 8.dp)
                                    ) {
                                        TextBasicIcon(
                                            text = "${item.startTime}-${item.endTime}",
                                            icon = Icons.Default.AccessTime
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
                                IconButton(onClick = {
                                    id.value = item.id
                                    if (id.value.isNotEmpty()) {
                                        openDialogDelete.value=true
//                                        vm.deleteReservation(id.value)
//                                        vm.deleteUserReservation(id.value)
//                                        vm.getAllUserReservationByDate(item.date)
                                        selectedReservation.value = item.toReservation()
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        tint = MaterialTheme.colorScheme.error,
                                        contentDescription = "deleteButton"
                                    )
                                }

                                IconButton(onClick = {
                                    editReservation(item)
                                }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        tint = confirmation,
                                        contentDescription = "editButton"
                                    )
                                }

                            }
                        }
                    }
                }
            }
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No reservations",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 24.sp
                )
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
            dayView.setBackgroundColor(md_theme_light_secondary.toArgb())
            dayView.setTextColor(md_theme_light_onSecondary.toArgb())
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


