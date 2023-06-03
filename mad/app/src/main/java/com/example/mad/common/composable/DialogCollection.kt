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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import com.example.mad.MainViewModel
import com.example.mad.common.getTimeStamp
import com.example.mad.common.getToday
import com.example.mad.model.Friend
import com.example.mad.model.Invitation
import com.example.mad.model.Playground
import com.example.mad.model.ProfileSport
import com.example.mad.model.Reservation
import com.example.mad.model.Stat
import com.example.mad.model.UserReservation
import com.example.mad.ui.theme.confirmation
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import com.stacktips.view.utils.CalendarUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


//ADD PAGE FOR HANDLING LIST OF FRIENDS


@Composable
fun FullDialogSport(
    openDialog: (Boolean) -> Unit,
    vm: MainViewModel,
    data: List<String>,
    changeUi: MutableState<Boolean>,
    sport: String = ""
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

    val context = LocalContext.current

    val topBarText = if(sport.isEmpty()) "Add your sport stat"
    else "Edit your sport stat"

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
                        text = topBarText, color = MaterialTheme.colorScheme.onSurface,
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

                            val trophies = if (trophiesText.isEmpty()) 0L
                            else if (trophiesText.isDigitsOnly()) trophiesText.toLong()
                            else {
                                -1L
                            }

                            if (trophies == -1L) {
                                Toast.makeText(
                                    context,
                                    "Invalid trophies field",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val ps =
                                    ProfileSport(
                                        sport = sport.ifEmpty { data[selected.value] },
                                        level = score.toLong(),
                                        trophies
                                    )

                                vm.insertUserSport(ps)
                                changeUi.value = true
                                openDialog(false)
                            }

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RectangleShape,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(text = "Save", color = confirmation)
                    }
                }
            }

            Column(Modifier.verticalScroll(rememberScrollState())) {


                //LIST SPORT SELECTABLE -> ADD DIALOG
                if (sport.isEmpty()) {
                    ListContainerDialog(modifier = Modifier.fillMaxWidth(), data, state = selected)
                }
                //SPORT -> EDIT DIALOG
                else {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(25.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Sport", fontSize = 20.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )

                        Text(
                            text = sport, fontSize = 20.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                /*LEVEL*/
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Level",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
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


}


@Composable
fun ListContainerDialog(
    modifier: Modifier = Modifier,
    data: List<String>,
    state: MutableState<Int>,
    text: String = "Select Sport",
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
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

    ListRadioButtonData(
        data, state, modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(15.dp)
    )
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
    vm: MainViewModel,
) {
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

    val friends = vm.friends.observeAsState().value ?: emptyList()

    val profile = vm.profile.observeAsState().value

    val confirmedFriends = remember {
        mutableStateOf<List<Friend>>(emptyList())
    }

    val stat = vm.statInvitation.value

    //handle tabs
    val state = remember { mutableStateOf(0) }

    val timeSlot = vm.availableTimeSlot.observeAsState().value?.toList() ?: emptyList()

    val icons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.AccessTime,
        Icons.Default.ShoppingBag,
        Icons.Default.Person,
        Icons.Default.EventAvailable
    )

    LaunchedEffect(key1 = null) {
        vm.getUserProfile()
        vm.getFriends()
        vm.getStatBySport(playground.sport)
    }

    Dialog(
        onDismissRequest = {
            openDialog.value = false
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
                        openDialog.value = false
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
                            if (selectedDate.value != "" && selectedTimeSlot.value != -1 && confirmEquipment.value) {
                                val email = vm.currentUser.value?.email ?: ""
                                val startTime =
                                    timeSlot[selectedTimeSlot.value].substringBefore("-")
                                val endTime = timeSlot[selectedTimeSlot.value].substringAfter("-")
                                val timestamp = getTimeStamp().toString()

                                if (email.isNotEmpty()) {

                                    if(confirmedFriends.value.isNotEmpty() && profile!=null){

                                        confirmedFriends.value.forEach {
                                            val i = Invitation(
                                                playground.address,
                                                playground.city,
                                                selectedDate.value,
                                                it.email,
                                                email,
                                                endTime,
                                                false,
                                                timestamp,
                                                playground.playground,
                                                playground.sport,
                                                startTime,
                                                profile.fullName,
                                                stat?.level?:0L,
                                                stat?.trophies?:0L
                                            )

                                            vm.sendInvitation(i)

                                        }
                                    }
                                    val r = Reservation(
                                        playground.address,
                                        playground.city,
                                        selectedDate.value,
                                        email,
                                        endTime,
                                        selectedEquipment.value,
                                        timestamp,
                                        playground.playground,
                                        playground.sport,
                                        startTime
                                    )
                                    val ur = UserReservation(
                                        playground.address,
                                        playground.city,
                                        selectedDate.value,
                                        endTime,
                                        selectedEquipment.value,
                                        timestamp,
                                        playground.playground,
                                        playground.sport,
                                        startTime
                                    )

                                    vm.insertReservation(timestamp, r)
                                    vm.insertUserReservation(timestamp, ur)
                                }


                                openDialog.value = false
                            }

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RectangleShape,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(text = "Save", color = confirmation)
                    }
                }
            }

            Column(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .fillMaxWidth()
            ) {

                TabRow(
                    selectedTabIndex = state.value,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    divider = {},
                ) {
                    icons.forEachIndexed { index, icon ->
                        Tab(
                            onClick = {
                                if (index == 0 && selectedDate.value.isNotEmpty()) state.value = 0
                                else if (index == 1 && selectedTimeSlot.value != -1) state.value = 1
                                else if (index == 2 && confirmEquipment.value) state.value = 2
                                else if (index == 3 ) state.value = 3
                            },
                            selected = (index == state.value)
                        ) {
                            Icon(imageVector = icon, contentDescription = "icon tab")
                        }
                    }
                }
                Column(
                    Modifier
                        .padding(vertical = 15.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    when (state.value) {
                        0 -> {
                            DatePicker(selectedDate, state, vm, playground.address, playground.city)
                        }

                        1 -> {
                            ListRadioButtonData(
                                data = timeSlot, state = selectedTimeSlot,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            )

                            Button(onClick = {
                                if (selectedTimeSlot.value != -1) state.value = 2
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = confirmation, contentColor = androidx.compose.ui.graphics.Color.White)
                                ) {
                                Text("Confirm",fontSize = 18.sp,
                                    style = MaterialTheme.typography.bodyLarge)
                            }
                        }

                        2 -> {
                            Column(
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 15.dp)
                                    .fillMaxWidth()
                            ) {

                                Text(
                                    text = "If you don't have the right equipment, you can rent it from the playground",
                                    fontSize = 18.sp, style = MaterialTheme.typography.bodyLarge
                                )

                                Row(
                                    Modifier
                                        .padding(vertical = 10.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "Equipment",
                                        modifier = Modifier.padding(end = 10.dp),
                                        fontSize = 18.sp,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Switch(checked = selectedEquipment.value, onCheckedChange = {
                                        selectedEquipment.value = !selectedEquipment.value
                                    })

                                }
                                Column(
                                    Modifier
                                        .padding(vertical = 15.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Button(onClick = {
                                        confirmEquipment.value = true
                                        if (friends.isNotEmpty())
                                            state.value = 3
                                        else state.value = 4
                                    },
                                        colors = ButtonDefaults.buttonColors(containerColor = confirmation, contentColor = androidx.compose.ui.graphics.Color.White)
                                    ) {
                                        Text(
                                            "Confirm",
                                            fontSize = 18.sp,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }

                        }

                        3 -> {
                            if (friends.isNotEmpty()) {
                                FriendsList(friends, confirmedFriends, state)
                            }
                        }

                        4 -> {
                            SummaryReservation(
                                playground = playground.playground,
                                date = selectedDate.value,
                                timeSlot = timeSlot[selectedTimeSlot.value],
                                equipment = selectedEquipment.value,
                                confirmedFriends.value.map { it.fullName },
                            )
                        }
                    }
                }
            }

        }

    }


}

@Composable
fun SummaryReservation(
    playground: String,
    date: String,
    timeSlot: String,
    equipment: Boolean,
    friendList: List<String>,
) {

    val expanded = remember {
        mutableStateOf(false)
    }


    Column {

        Text(
            text = playground,
            fontSize = 20.sp, style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Row(Modifier.padding(vertical = 10.dp)) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "calendar"
                    )

                    Text(
                        text = date,
                        modifier = Modifier.padding(start = 10.dp),
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "time"
                    )

                    Text(
                        text = timeSlot,
                        modifier = Modifier.padding(start = 10.dp),
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }

        }

        Row(Modifier.padding(vertical = 10.dp)) {
            Text(
                text = "Equipment ",
                fontSize = 18.sp, style = MaterialTheme.typography.bodyLarge
            )

            Icon(
                imageVector = Icons.Default.CheckCircle,
                tint = if (equipment) androidx.compose.ui.graphics.Color.Green
                else androidx.compose.ui.graphics.Color.Gray,
                contentDescription = "calendar",
                modifier = Modifier.padding(start = 10.dp)
            )

        }
        if (friendList.isNotEmpty()) {
            Divider(Modifier.padding(vertical = 15.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded.value = !expanded.value },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Invited Friends",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(onClick = { expanded.value = !expanded.value }) {
                        Icon(
                            imageVector =
                            if (!expanded.value) Icons.Default.KeyboardArrowDown
                            else Icons.Default.KeyboardArrowUp, contentDescription = "friends"
                        )
                    }
                }

            }
            if (expanded.value) {
                LazyColumn(
                    Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(friendList) { item ->
                        Text(
                            text = item,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Divider()

                    }
                }
            }
        }


    }
}

@Composable
fun FriendsList(
    friends: List<Friend>,
    confirmedFriends: MutableState<List<Friend>>,
    state: MutableState<Int>
) {

    val context = LocalContext.current

    val selectedFriends = remember {
        mutableStateOf(confirmedFriends.value.ifEmpty { friends.map { Friend("","","") } })
    }

    fun getListOfFriend(f: Friend, i: Int): List<Friend> {
        val l = mutableListOf<Friend>()
        selectedFriends.value.forEachIndexed { index, value ->
            if (i == index)
                l.add(f)
            else
                l.add(value)
        }
        return l.toList()
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Invite your friends",
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    Column {
        LazyColumn(
            Modifier
                .height(150.dp)
                .fillMaxWidth()
                .padding(vertical = 15.dp)
        ) {
            itemsIndexed(friends) { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            if (selectedFriends.value[index].email == item.email)
                                selectedFriends.value = getListOfFriend(Friend("", "", ""), index)
                            else
                                selectedFriends.value = getListOfFriend(item, index)
                        }
                ) {
                    RadioButton(
                        selected = selectedFriends.value[index].email == item.email, onClick = {
                            if (selectedFriends.value[index].email == item.email)
                                selectedFriends.value = getListOfFriend(Friend("","",""), index)
                            else
                                selectedFriends.value = getListOfFriend(item, index)
                        }, Modifier.padding(horizontal = 10.dp)
                    )

                    Text(
                        text = item.fullName,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }


    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    state.value = 4
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Skip", fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                val condition = selectedFriends.value.filterNot { it.email.isEmpty() }.size==selectedFriends.value.size
                if(condition){
                    confirmedFriends.value = selectedFriends.value
                    state.value = 4
                }
                else {
                    Toast.makeText(context,"Please select a person",Toast.LENGTH_SHORT).show()
                }

            }, colors = ButtonDefaults.buttonColors(containerColor = confirmation, contentColor = androidx.compose.ui.graphics.Color.White)) {
                Text(
                    text = "Confirm",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }


}

@Composable
fun ListRadioButtonData(
    data: List<String>,
    state: MutableState<Int>,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier) {
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

class DisabledColorDecorator : DayDecorator {
    override fun decorate(dayView: DayView) {
        if (CalendarUtils.isPastDay(dayView.date)) {
            val color: Int = Color.parseColor("#a9afb9")
            dayView.setBackgroundColor(color)
        }

    }
}

class SelectedColorDecorator(private val dateText: String) : DayDecorator {
    override fun decorate(dayView: DayView) {
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dayView.date?.let { df.format(it) } ?: getToday()

        if (formattedDate == dateText) {
            dayView.setBackgroundColor(Color.GREEN)
        }

    }
}

@Composable
fun DatePicker(
    selectedDate: MutableState<String>,
    state: MutableState<Int>,
    vm: MainViewModel,
    address: String,
    city: String,
) {
    AndroidView(factory = { CustomCalendarView(it) }, update = {

        val listDecorator: MutableList<DayDecorator> = mutableListOf()
        listDecorator.add(DisabledColorDecorator())
        if (selectedDate.value != "") {
            listDecorator.add(SelectedColorDecorator(selectedDate.value))
        }
        it.decorators = listDecorator
        it.refreshCalendar(Calendar.getInstance(Locale.getDefault()))

        it.setCalendarListener(object : CalendarListener {

            override fun onDateSelected(date: Date?) {

                val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val today = getToday()

                val dateText = date?.let { d -> df.format(d) } ?: today

                if (dateText >= today) {
                    vm.getTimeSlotReservationByPlaygroundAndDate(dateText, address, city)
                    selectedDate.value = dateText
                    state.value = 1
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