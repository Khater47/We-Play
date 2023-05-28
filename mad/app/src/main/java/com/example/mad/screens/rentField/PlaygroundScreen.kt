package com.example.mad.screens.rentField

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.common.getTimeSlot
import com.example.mad.common.getToday
import com.example.mad.model.Comment
import com.example.mad.model.Playground
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlaygroundScreen(
    vm: MainViewModel,
    navController: NavHostController,
    playgroundId: String?
) {

//    val loading = vm.loadingProgressBar.value

    val image = R.drawable.field

    val p = remember {
        mutableStateOf(
            Playground(
                id="",
                phone="",
                email="",
                openHours="",
                playground="",
                sport="",
                city="",
                address=""
            )
        )
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    vm.getPlaygroundById(playgroundId ?: "0").observe(lifecycleOwner) {

        p.value = Playground(
            id=playgroundId?:"",
            phone=it?.phone ?: "",
            email=it?.email ?: "",
            openHours= it?.openHours ?: "",
            playground=it?.playground ?: "",
            sport=it?.sport ?: "",
            city=it?.city ?: "",
            address= it?.address ?: ""
        )
    }

   val comments = remember {
       mutableStateOf<List<Comment>>(
           emptyList()
       )
   }

    var avg = remember {
        mutableStateOf(0f)
    }
    var totalQuality = remember {
        mutableStateOf(0f)
    }
    var totalFacilities = remember {
        mutableStateOf(0f)
    }

    vm.getPlaygroundsComments(playgroundId?:"0").observe(lifecycleOwner){

        val list = it.filterNotNull()
        var quality = 0f
        var facilities = 0f
        val count = list.size
        comments.value = list
        list.forEach {
            comment ->
            quality += comment.quality.toFloat()
            facilities += comment.facilities.toFloat()
        }
        totalQuality.value = quality/count
        totalFacilities.value = facilities/count
//        avg = ((totalQuality + totalFacilities) / 2).roundToInt().toFloat()
        avg.value = (totalQuality.value+totalFacilities.value)/2

    }

    val selectedDate = remember { mutableStateOf(getToday()) }
    val showDialog = remember { mutableStateOf(false) }
    val showConfirmReservationDialogue = remember { mutableStateOf(false) }
    val showTimeSlotsDialog = remember { mutableStateOf(false) }
    val selectedTimeSlot = remember { mutableStateOf("") }

    val timeSlots = getTimeSlot()

    val selectedTimeSlotIndex = remember { mutableStateOf<Int>(-1) }


    fun goToPreviousPage() {
        navController.navigate(BottomBarScreen.SearchField.route)
    }

    Scaffold(
        topBar = {
            TopBarBackButton(R.string.topBarPlayground, ::goToPreviousPage)
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    // Field Image
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                // Name and rating
                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier
                        .weight(4f)
                        .padding(horizontal = 15.dp)) {
                        Text(
                            text = p.value.playground,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.StarRate, contentDescription = "",
                        modifier=Modifier.padding(end=5.dp))
                        Text(
                            text = "${avg.value.toInt()}",
                        )
                    }

                }


                // Information Section
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                ) {
                    // Card content
                    InfoLine(icon = Icons.Default.LocationOn, text = p.value.address)
                    Divider(thickness = 1.dp)
                    InfoLine(icon = Icons.Default.Call, text = p.value.phone)
                    Divider(thickness = 1.dp)
                    InfoLine(icon = Icons.Default.Mail, text = p.value.email)
                    Divider(thickness = 1.dp)
                    InfoLine(icon = Icons.Default.Schedule, text = p.value.openHours)
                }

                // Rating card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = CardDefaults.outlinedCardBorder(),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Rating(text = "Quality", score = totalQuality.value.toInt())
                    Rating(text = "Facilities", score = totalFacilities.value.toInt())
                }

                // Users Comments
                UserComments(comments.value)

                //Divider()

                Column(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Custom date picker
                    OutlinedButton(
                        onClick = { showDialog.value = true },
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    )
                    {
                        Text(
                            "Reserve Playground",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    }



                    if (showDialog.value) {
                        DatePickerDialog(
                            selectedDate = selectedDate,
                            onDismiss = { showDialog.value = false },
                            next = { showTimeSlotsDialog.value = true }
                        )
                    }

                    if (showTimeSlotsDialog.value) {
                        TimeSlotPickerDialog(
                            timeSlots = timeSlots,
                            selectedTimeSlot = selectedTimeSlot,
                            onDismiss = { showTimeSlotsDialog.value = false },
                            back = { showDialog.value = true },
                            selectedTimeSlotIndex = selectedTimeSlotIndex,
                            confirm = { showConfirmReservationDialogue.value = true }
                        )
                    }

                    if (showConfirmReservationDialogue.value) {
                        ReservationConfirmationDialogue(
                            vm,
                            playGroundName = p.value.playground,
                            reservationDay = selectedDate.value,
                            selectedTimeSlot = selectedTimeSlot.value,
                            onDismiss = { showConfirmReservationDialogue.value = false }
                        )
                    }
                }

            }
//            CircularProgressBar(isDisplayed = loading)

        }
    }


}


@Composable
fun InfoLine(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Icon(imageVector = icon, contentDescription = "Location", modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.padding(horizontal = 12.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun Rating(text: String, score: Int) {
    Row(modifier = Modifier.padding(16.dp)) {
//        TextBasicHeadLine(text = text)
        Text(
            text = text, style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(weight = 1f)
        )
        Spacer(modifier = Modifier.padding(horizontal = 20.dp))
        Row(modifier = Modifier.weight(2f)) {
            for (num in 1..score) {
                Icon(
                    imageVector = Icons.Filled.Grade, contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
            }
            for (num in 1..(5 - score)) {
                Icon(
                    imageVector = Icons.Outlined.Grade, contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun UserComments(comments: List<Comment>) {

    val (isExpanded, setIsExpanded) = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 7.dp)
        ) {

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        setIsExpanded(!isExpanded)
                    },
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.padding(horizontal = 30.dp))
                if (isExpanded) {
                    IconButton(onClick = { setIsExpanded(false) }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Expand"
                        )
                    }
                } else {
                    IconButton(onClick = { setIsExpanded(true) }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand"
                        )
                    }
                }

            }


            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    (comments).forEach {
                        CommentCard(comment = it)
                    }
                }
            }

        }

    }

}


@Composable
fun CommentCard(comment: Comment) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "person",
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Top)
        )
        Spacer(modifier = Modifier.padding(horizontal = 3.dp))
        Card(
            border = CardDefaults.outlinedCardBorder(), modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = comment.nickname,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(7.dp)
                )
                Text(
                    text = comment.comment, style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(7.dp)
                )
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDialog(
    selectedDate: MutableState<String>,
    onDismiss: () -> Unit,
    next: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text("Cancel", color = Color.White)
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Button(
                    onClick = {
                        onDismiss()
                        next()
                    },
                    modifier = Modifier.weight(2f),
                    colors = ButtonDefaults.buttonColors(Color.Black)

                ) {
                    Text("Select Timeslot", color = Color.White)
                }
            }
        },
        title = {
            Text("Select Reservation Date", style = MaterialTheme.typography.titleMedium)
        },
        text = {
            DatePicker(
                selectedDate = selectedDate
            )
        },
        shape = RoundedCornerShape(5)
    )
}


@Composable
fun DatePicker(
    selectedDate: MutableState<String>

) {
    AndroidView(factory = { CustomCalendarView(it) }, update = {
        it.setCalendarListener(object : CalendarListener {

            override fun onDateSelected(date: Date?) {
                val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val dateText = date?.let { d -> df.format(d) }

                val formattedDate = if (dateText.isNullOrEmpty()) selectedDate else dateText

                selectedDate.value = formattedDate as String

            }

            override fun onMonthChanged(date: Date?) {}
        })
    })
}


@Composable
fun TimeSlotPickerDialog(
    timeSlots: List<String>,
    selectedTimeSlot: MutableState<String>,
    onDismiss: () -> Unit,
    back: () -> Unit,
    selectedTimeSlotIndex: MutableState<Int>,
    confirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.height(500.dp),
        buttons = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Button(
                    onClick = {
                        onDismiss()
                        back()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text("Back", color = Color.White)
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Button(
                    onClick = {
                        onDismiss()
                        confirm()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text("Confirm", color = Color.White)
                }
            }
        },
        title = {},
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(timeSlots) { index, item ->
                    TextButton(onClick = {
                        selectedTimeSlot.value = item
                        selectedTimeSlotIndex.value = index
                    }) {
                        Text(
                            text = item, color =
                            if (selectedTimeSlotIndex.value == index) Color.Blue else Color.Black
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(5)
    )
}

@Composable
fun ReservationConfirmationDialogue(
    vm:MainViewModel,
    playGroundName: String,
    reservationDay: String,
    selectedTimeSlot: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel", color = Color.Red)
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                OutlinedButton(
                    onClick = {
                        Log.d("RESERVATION","$reservationDay $selectedTimeSlot")
//                        vm.insertReservation(reservationId="",reservation=)
//                        vm.insertUserReservation(userId="",reservationId="",reservation=)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)

                ) {
                    Text("Confirm", color = Color(0xff005a00))
                }
            }
        },
        title = {
            Text("Confirm Reservation", style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Text(text = "Confirm reservation of $playGroundName on $reservationDay for the timeslot $selectedTimeSlot")
        },
        shape = RoundedCornerShape(5)
    )
}

fun computeOverallRating(quality: Int, facilities: Int): String {
    val rating = (quality + facilities) / 2.0
    return rating.toString()
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun PlayGroundsScreenPreview() {
//    MadTheme {
//        PlaygroundScreen()
//    }
//}

/*@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true)
@Composable
fun CommentsSectionPreview() {
    val comments = listOf(
        Comment("Giorgio", "Nice football playground"),
        Comment("Paolo", "Recommended"),
        Comment("Ahmed", "The playground is good, but the facilities need to be improved"),
        Comment("Davide", "Golf field is nice!")
    )
    MadTheme {
        Expandable(comments)
    }
}

@Preview(showBackground = true)
@Composable
fun CommentCardPreview() {
    val comment = Comment("AXE47", "Nice playground, Recommended!")
    MadTheme {
        CommentCard(comment)
    }
}*/