package com.example.mad.reservation


import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Card
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalConfiguration
import com.example.mad.home.TopAppBarHome
import com.example.mad.model.Reservation
import com.example.mad.utils.formatDate
import com.example.mad.utils.getIconSport
import com.example.mad.utils.getToday
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
fun ReservationScreen(navController: NavHostController, vm: UserViewModel) {

    val today = getToday()

    val dateState = remember {
        mutableStateOf(
            today
        )
    }

    val playgroundList = vm.playgrounds.observeAsState().value ?: emptyList()

    val reservationPlayground = mutableListOf<ReservationPlayground>()

    val reservationDate = vm.getReservationByDate(dateState.value).observeAsState().value?:emptyList()

    reservationDate.forEach { r ->
        val p = playgroundList.firstOrNull { r.idPlayground == it.id }
        if (p != null) {
            reservationPlayground.add(
                ReservationPlayground(r.id, r.date, p.sport, p.playground,r.idPlayground, p.location, r.equipment, r.startTime, r.endTime, r.idProfile)

            )
        }
    }

    //get all date from existing reservation for highlight days
    val dates = vm.reservationsDate.observeAsState().value?: emptyList()

    val orientation = LocalConfiguration.current.orientation

    Scaffold(
        topBar = { TopAppBarReservation() }
    ) {
        Column(
            Modifier
                .padding(it)
        ){
            when(orientation){
                Configuration.ORIENTATION_PORTRAIT -> {
                    Column(Modifier.weight(1f)){

                        CalendarCard(
                            dates,
                            today,
                            dateState
                        )
                    }
                    Column(Modifier.weight(1f)){

                        ReservationContainer(
                            dateState,
                            reservationPlayground,
                            navController,
                            vm
                        )
                    }
                }
                else -> {
                    Row(
                        Modifier
                            .fillMaxHeight()){
                        Column(
                            Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())){

                            CalendarCard(
                                dates,
                                today,
                                dateState
                            )
                        }
                        Column(Modifier.weight(1f)){

                            ReservationContainer(
                                dateState,
                                reservationPlayground,
                                navController,
                                vm
                            )
                        }
                    }
                }

            }

        }

    }

}


@Composable
fun CalendarCard(
    dates:List<String>,
    today:String,
    dateState: MutableState<String>
){
    Card(
        modifier=Modifier.padding(10.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        AndroidView(factory = { CustomCalendarView(it) }, update = {

            if(dates.isNotEmpty()){
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

@Composable
fun ReservationContainer(
    dateState: MutableState<String>,
    reservationPlayground:List<ReservationPlayground>,
    navController:NavHostController,
    vm:UserViewModel
){
    val orientation = LocalConfiguration.current.orientation
    val modifier = if(orientation==Configuration.ORIENTATION_PORTRAIT){
        Modifier.fillMaxWidth()
    }
    else {
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    }
    Column(modifier=modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(text = dateState.value,fontSize=20.sp)
    }

    ReservationCard(reservationPlayground.filter { (it.date == dateState.value) }, navController,vm)

}


@Composable
fun ReservationCard(
    reservationList: List<ReservationPlayground>,
    navController: NavHostController,
    vm: UserViewModel,
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
                                text = item.playground,
                                fontSize=20.sp
                            )

                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)) {
                                Row{
                                    Icon(imageVector = getIconSport(item.sport), contentDescription = "Sport")

                                    Text(text = item.sport,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier=Modifier.padding(horizontal=5.dp)
                                    )

                                }
                            }

                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)) {
                                Row{
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location")
                                    Text(
                                        text = item.location,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier=Modifier.padding(horizontal=5.dp)
                                    )
                                }
                            }


                        }


                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center) {
                            Row{
                                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Time")

                                Text(
                                    text = item.startTime+"-"+item.endTime,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier=Modifier.padding(horizontal=10.dp)
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
                                val r = Reservation(
                                    item.id,
                                    item.date,
                                    item.equipment,
                                    item.idPlayground,
                                    item.startTime,
                                    item.endTime,
                                    item.idProfile
                                )
                                vm.deleteReservation(r)
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
) {


    TopAppBar(
        title = {
            Text(
                text = "Reservation",
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )

        },
        navigationIcon = {},
        actions = {},
        elevation = 10.dp

    )
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


data class ReservationPlayground(
    val id: Int,
    val date: String,
    val sport: String,
    val playground: String,
    val idPlayground:Int,
    val location: String,
    val equipment: Int,
    val startTime: String,
    val endTime: String,
    val idProfile: Int
)


/*@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ReservationScreen()
    }
}*/
