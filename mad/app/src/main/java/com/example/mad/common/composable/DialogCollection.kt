package com.example.mad.common.composable

import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mad.MainViewModel
import com.example.mad.common.getSport
import com.example.mad.common.getTimeSlot
import com.example.mad.common.getToday
import com.example.mad.model.Playground
import com.example.mad.model.ProfileSport
import com.example.mad.model.Reservation
import com.example.mad.model.UserReservation
import com.example.mad.screens.reservation.ColorDayDecorator
import com.example.mad.ui.theme.MadTheme
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import com.stacktips.view.utils.CalendarUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DialogList(
    data: List<String>,
    text: String,
    openDialog: (Boolean) -> Unit,
    setData: (String) -> Unit
) {

    val selected = remember {
        mutableStateOf(-1)
    }



    Dialog(
        onDismissRequest = {openDialog(false) },
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            ListContainerDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp), data = data,
                state = selected,
                text = text,
                horizontalAlignment = Alignment.Start
            )

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Column(Modifier.padding(horizontal = 10.dp)) {
                    Button(
                        onClick = { openDialog(false) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Cancel", fontSize = 14.sp, style = MaterialTheme.typography.bodySmall)
                    }

                }
                Column(Modifier.padding(end = 10.dp)) {
                    Button(
                        onClick = {
                            val dataString = if(selected.value==-1) "" else data[selected.value]
                            setData(dataString)
                            openDialog(false) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            "Confirm",
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                }
            }
        }

    }

}

@Composable
fun FullDialogSport(
    openDialog: (Boolean) -> Unit,
    vm: MainViewModel,
    data: List<String>
) {

    val (trophiesText, setTrophiesText) = remember {
        mutableStateOf("")
    }

    val (score, setScore) = remember {
        mutableStateOf(0)
    }
    val selected = remember {
        mutableStateOf(0)
    }


    val description = "Add or change your favorites sport and the related statistics, " +
            "like level from 1 to 5 and the " +
            "number of trophies that you have earn it"

    Dialog(
        onDismissRequest = {
            openDialog(false)
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface,
                    RectangleShape
                ),
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
                        openDialog(false)
                    }, modifier = Modifier.fillMaxHeight()) {
                        Icon(
                            imageVector = Icons.Default.Clear, contentDescription = "close dialog",
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
                        text = "Set sport", color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium
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

                            val trophies = if (trophiesText == "") 0L else trophiesText.toLong()

                            val ps =
                                ProfileSport(
                                    sport = data[selected.value],
                                    level = score.toLong(),
                                    trophies
                                )
                            val userId = vm.currentUser?.email?:""

                            vm.insertUserProfileSport(userId, ps)
                            openDialog(false)
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

            /*DESCRIPTION DIALOG TEXT*/
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    description, fontSize = 15.sp, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            ListContainerDialog(data, state = selected)

            /*LEVEL*/
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Level", fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButtonRating(score, setScore)
            }

            MedalItemDialog(text = "Trophies", medal = trophiesText, setMedal = setTrophiesText)

        }
    }


}



@Composable
fun ListContainerDialog(
    data: List<String>,
    state: MutableState<Int>,
    text: String = "Select Sport",
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    modifier: Modifier = Modifier.fillMaxWidth()
) {


    Column(
        modifier = modifier, horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text, fontSize = 20.sp, style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Divider()
    }

    ListRadioButtonData(data, state,modifier= Modifier
        .height(200.dp)
        .fillMaxWidth()
        .padding(15.dp) )
    Divider()
}


@Composable
fun MedalItemDialog(
    text: String,
    medal: String,
    setMedal: (String) -> Unit,
) {


    Column(
        Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = text, fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = medal,
            onValueChange = {
                setMedal(it)
            },
            label = { Text(text = text) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
        )


    }
}


@Composable
fun FullDialogPlayground(
    openDialog: MutableState<Boolean>,
    playground: Playground,
    vm:MainViewModel
    ){
    val selectedDate = remember {
        mutableStateOf("")
    }
    val selectedTimeSlot = remember {
        mutableStateOf(-1)
    }
    val selectedEquipment = remember {
        mutableStateOf(false)
    }
    val confirmEquipment = remember {
        mutableStateOf(false)
    }

    val state = remember { mutableStateOf(0) }

    val timeSlot = getTimeSlot()

    val icons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.AccessTime,
        Icons.Default.ShoppingBag,
        Icons.Default.EventAvailable
        )


    Dialog(
        onDismissRequest = {
            openDialog.value=false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface,
                    RectangleShape
                ),
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
                        openDialog.value=false
                    }, modifier = Modifier.fillMaxHeight()) {
                        Icon(
                            imageVector = Icons.Default.Clear, contentDescription = "close dialog",
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
                        text = "Rent Playground", color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium
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
                            if(selectedDate.value!="" && selectedTimeSlot.value!=-1 && confirmEquipment.value){
                                val email = vm.currentUser?.email?:""
                                val startTime = timeSlot[selectedTimeSlot.value].substringBefore("/")
                                val endTime = timeSlot[selectedTimeSlot.value].substringAfter("/")

                                val r = Reservation(
                                    playground.address,
                                    playground.city,
                                    selectedDate.value,
                                    email,
                                    startTime,
                                    selectedEquipment.value,
                                    playground.playground,
                                    playground.sport,
                                    endTime
                                )
                                val ur = UserReservation(
                                    playground.address,
                                    playground.city,
                                    selectedDate.value,
                                    endTime,
                                    selectedEquipment.value,
                                    playground.playground,
                                    playground.sport,
                                    startTime
                                )

                                Log.d("RESERVATION",r.toString())
                                Log.d("RESERVATION",ur.toString())

                                openDialog.value=false
                            }

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

            Column(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .fillMaxWidth()) {

                TabRow(selectedTabIndex = state.value,
                    backgroundColor=MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    divider = {},
                ) {
                    icons.forEachIndexed { index, icon ->
                        Tab(
                            onClick = {
                                if(index==0 && selectedDate.value.isNotEmpty()) state.value=0
                                else if(index==1 && selectedTimeSlot.value!=-1) state.value=1
                                else if(index==2 && confirmEquipment.value) state.value=2
                            },
                            selected = (index == state.value)
                        ){
                            Icon(imageVector = icon, contentDescription = "icon tab")
                        }
                    }
                }
                Column(
                    Modifier
                        .padding(vertical = 15.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center){

                    when(state.value){
                        0 -> {
                            DatePicker(selectedDate,state)
                        }
                        1 -> {
                                ListRadioButtonData(data = timeSlot, state = selectedTimeSlot,
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp))

                                Button(onClick = {
                                    if(selectedTimeSlot.value!=-1) state.value=2
                                }) {
                                    Text("Confirm")
                                }
                        }
                        2 -> {
                            Column(
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 15.dp)
                                    .fillMaxWidth()) {

                                Text(text = "If you don't have the right equipment, you can rent it from the playground",
                                    fontSize = 18.sp,style=MaterialTheme.typography.bodyLarge)

                                Row(
                                    Modifier
                                        .padding(vertical = 10.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically){

                                    Text(text = "Equipment",modifier=Modifier.padding(end=10.dp),
                                        fontSize = 18.sp,style=MaterialTheme.typography.bodyLarge)
                                    Switch(checked = selectedEquipment.value, onCheckedChange = {
                                        selectedEquipment.value=!selectedEquipment.value
                                    })

                                }
                                Column(
                                    Modifier
                                        .padding(vertical = 15.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Button(onClick = { confirmEquipment.value=true
                                            state.value=3
                                        }) {
                                            Text("Confirm",fontSize = 18.sp,style=MaterialTheme.typography.bodyLarge)
                                        }
                                }
                            }

                        }
                        3 -> {
                            Column {
                                Text(text = "Are you sure to rent the playground with this info",
                                fontSize=20.sp,style=MaterialTheme.typography.bodyLarge,
                                modifier=Modifier.padding(vertical=20.dp))


                                Text(text = playground.playground,
                                    fontSize=18.sp,style=MaterialTheme.typography.bodyLarge,
                                    modifier=Modifier.padding(vertical=10.dp))

                                Row(Modifier.padding(vertical=10.dp)){
                                    Column(Modifier.weight(1f)){
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "calendar")

                                            Text(text = selectedDate.value,modifier=Modifier.padding(start=10.dp),
                                                fontSize=18.sp,style=MaterialTheme.typography.bodyLarge)
                                        }

                                    }
                                    Column(Modifier.weight(1f)){
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Icon(imageVector = Icons.Default.AccessTime, contentDescription = "time")

                                            Text(text = timeSlot[selectedTimeSlot.value],modifier=Modifier.padding(start=10.dp),
                                                fontSize=18.sp,style=MaterialTheme.typography.bodyLarge)
                                        }

                                    }

                                }

                                Row(Modifier.padding(vertical=10.dp)){
                                    Text(text = "Equipment ",
                                        fontSize=18.sp,style=MaterialTheme.typography.bodyLarge)

                                    Icon(imageVector = Icons.Default.CheckCircle,
                                        tint= if(selectedEquipment.value) androidx.compose.ui.graphics.Color.Green
                                        else androidx.compose.ui.graphics.Color.Gray
                                        , contentDescription = "calendar",
                                        modifier=Modifier.padding(start=10.dp))

                                }
                            }
                        }
                    }
                }
            }

        }

    }



}

@Composable
fun ListRadioButtonData(
    data:List<String>,
    state:MutableState<Int>,
    modifier:Modifier = Modifier
){

    LazyColumn(modifier=modifier) {
        itemsIndexed(data) { index, item ->
            Column(
                Modifier
                    .fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { state.value = index }
                ) {
                    RadioButton(selected = state.value == index, onClick = {
                        state.value = index
                    }, Modifier.padding(horizontal = 10.dp))

                    Text(text = item, fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

class DisabledColorDecorator() : DayDecorator {
    override fun decorate(dayView: DayView) {
        if (CalendarUtils.isPastDay(dayView.date)) {
            val color: Int = android.graphics.Color.parseColor("#a9afb9")
            dayView.setBackgroundColor(color)
        }

    }
}

class SelectedColorDecorator(private val dateText:String) : DayDecorator {
    override fun decorate(dayView: DayView) {
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dayView.date?.let { df.format(it) }?: getToday()

        if (formattedDate==dateText) {
            dayView.setBackgroundColor(Color.GREEN)
        }

    }
}

@Composable
fun DatePicker(
    selectedDate:MutableState<String>,
    state: MutableState<Int>
) {
    AndroidView(factory = { CustomCalendarView(it) }, update = {

        val listDecorator: MutableList<DayDecorator> = mutableListOf()
        listDecorator.add(DisabledColorDecorator())
        if(selectedDate.value!=""){
            listDecorator.add(SelectedColorDecorator(selectedDate.value))
        }
        it.decorators = listDecorator
        it.refreshCalendar(Calendar.getInstance(Locale.getDefault()))

        it.setCalendarListener(object : CalendarListener {

            override fun onDateSelected(date: Date?) {

                val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val today = getToday()

                val dateText = date?.let { d -> df.format(d) } ?: today

                if(dateText>=today){

                    selectedDate.value=dateText
                    state.value=1
                }


            }

            override fun onMonthChanged(date: Date?) {}
        })
    })
}

//@Preview(showBackground = true,device="spec:width=411dp,height=891dp")
//@Composable
//fun DefaultPreviewDialogTab(){
//
//    val openDialog = remember {
//        mutableStateOf(false)
//    }
//    MadTheme {
//        FullDialogPlayground(openDialog)
//    }
//}

//@Preview(showBackground = true,device="spec:width=411dp,height=891dp")
//@Composable
//fun DefaultPreviewDialog() {
//
//    val sportList = listOf(
//        "Soccer",
//        "Volleyball",
//        "Basket",
//        "Cricket",
//    )
//    val (isOpenSportDialog, openSportDialog) = remember {
//        mutableStateOf(false)
//    }
//    val (sport, setSport) = remember {
//        mutableStateOf(0)
//    }
//    val (gold,setGold) = remember {
//        mutableStateOf(0)
//    }
//    val (silver,setSilver) = remember {
//        mutableStateOf(0)
//    }
//    val (bronze,setBronze) = remember {
//        mutableStateOf(0)
//    }
//
//
//    MadTheme {
////            DialogList(sportList,openSportDialog,setSport)
//        FullDialogSport(openSportDialog)
//    }
//}

//device="spec:width=411dp,height=891dp,orientation=landscape"