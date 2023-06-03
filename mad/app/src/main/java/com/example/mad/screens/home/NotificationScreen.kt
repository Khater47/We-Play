package com.example.mad.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.common.getIconSport
import com.example.mad.model.Invitation
import com.example.mad.model.UserReservation
import com.example.mad.screens.profile.TextIcon
import com.example.mad.ui.theme.confirmation

//GET REPOSITORY INVITATION IN A ROOT COLLECTION = INVITATIONS (NOT AS SUB COLLECTION OF USER)
//ADD SUB COLLECTION OF FRIENDS FOR USER

/*
TODO
     2) confirmation dialog
 */

@Composable
fun NotificationScreen(
    navController: NavHostController,
    vm: MainViewModel
) {

    val invitations = vm.invitation.observeAsState().value ?: emptyList()

    val changeUi = remember {
        mutableStateOf(true)
    }


    LaunchedEffect(key1 = changeUi.value) {
        if(changeUi.value){
            vm.getInvitations()
            changeUi.value=false
        }
    }


    val loading = vm.loadingProgressBar.value

    fun navigate() {
        navController.navigate(BottomBarScreen.Home.route)
    }


    Scaffold(
        topBar = { TopBarBackButton(R.string.topBarNotifications, ::navigate) }
    ) {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                items(invitations) { item ->
                    CardNotification(item,vm,changeUi)
                }
            }
            CircularProgressBar(isDisplayed = loading)
        }
    }
}

@Composable
fun CardNotification(
    notification: Invitation,
    vm: MainViewModel,
    changeUi: MutableState<Boolean>
) {

    val showStat = remember {
        mutableStateOf(false)
    }

    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Column(Modifier.padding(horizontal = 10.dp)) {

            Text(
                text = notification.playground,
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            Column(Modifier.padding(vertical = 10.dp)) {
                TextIcon(
                    text = notification.address + " " + notification.city,
                    icon = Icons.Default.LocationOn
                )
            }
            Column(Modifier.padding(vertical = 10.dp)) {
                TextIcon(text = notification.sport, icon = getIconSport(notification.sport))
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    TextIcon(text = notification.date, icon = Icons.Default.CalendarMonth)
                }
                Column(Modifier.weight(1f)) {
                    TextIcon(
                        text = notification.startTime + "-" + notification.endTime,
                        icon = Icons.Default.AccessTime
                    )
                }
            }


            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickable {
                        showStat.value = !showStat.value
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = notification.fullName,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(onClick = {
                        showStat.value = !showStat.value
                    }) {
                        Icon(
                            imageVector = if (!showStat.value) Icons.Default.KeyboardArrowDown
                            else Icons.Default.KeyboardArrowUp,
                            contentDescription = "show stat user"
                        )
                    }

                }

            }

            if (showStat.value) {
                UserStat(notification.level, notification.trophies)
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val id = notification.id + " " + notification.emailReceiver
                            vm.deleteInvitation(id)
                            changeUi.value=true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Decline",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }


                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Button(
                        onClick = {
                            val id = notification.id + " " + notification.emailReceiver
                            val ur = UserReservation(
                                notification.address,
                                notification.city,
                                notification.date,
                                notification.endTime,
                                false,
                                notification.id,
                                notification.playground,
                                notification.sport,
                                notification.startTime,
                            )
                            vm.deleteInvitation(id)
                            vm.insertUserReservation(notification.id,ur)
                            changeUi.value=true

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = confirmation,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Accept",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }
                }
            }


        }
    }
}

@Composable
fun UserStat(level: Long, trophies: Long) {

    Column(
        Modifier
            .padding(vertical = 10.dp)
    ) {
        Row {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Level", fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Column(Modifier.weight(1f)) {
                TextIcon(text = "$level/5", icon = Icons.Default.Star)
            }

        }
    }
    Column(
        Modifier.padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Trophies",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

            }
            Column(Modifier.weight(1f)) {
                TextIcon(text = "$trophies", icon = Icons.Default.EmojiEvents)
            }

        }
    }
}

