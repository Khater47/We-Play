package com.example.mad.common.composable

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mad.common.getIconPlayground
import com.example.mad.common.getIconSport
import com.example.mad.model.Playground
import com.example.mad.model.Reservation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPlayground(playground: Playground){

    val heightImage =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            200.dp
        } else {
            140.dp
        }


    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column {
            Column(Modifier.padding(10.dp)) {
                TextBasicHeadLine(text = playground.playground)
            }

            Image(
                painter = painterResource(id = getIconPlayground(playground.sport)),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(heightImage)
                    .fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Spacer(Modifier.padding(vertical=5.dp))
                    TextBasicIcon(text = playground.location, icon = Icons.Default.LocationOn)
                    Spacer(Modifier.padding(vertical=5.dp))
                }
                Column(Modifier.weight(1f)) {
                    Spacer(Modifier.padding(vertical=5.dp))
                    TextBasicIcon(text = playground.sport, icon = getIconSport(playground.sport))
                    Spacer(Modifier.padding(vertical=5.dp))
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardReservation(reservation: Reservation){

    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column {
            Column(Modifier.padding(10.dp)) {
                TextBasicHeadLine(text = reservation.playground)
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Spacer(Modifier.padding(vertical=5.dp))
                    TextBasicIcon(text = reservation.city, icon = Icons.Default.LocationOn)
                    Spacer(Modifier.padding(vertical=5.dp))
                }
                Column(Modifier.weight(1f)) {
                    Spacer(Modifier.padding(vertical=5.dp))
                    TextBasicIcon(text = reservation.sport, icon = getIconSport(reservation.sport))
                    Spacer(Modifier.padding(vertical=5.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Spacer(Modifier.padding(vertical=5.dp))
                    TextBasicIcon(text = reservation.date, icon = Icons.Default.CalendarMonth)
                    Spacer(Modifier.padding(vertical=5.dp))
                }
                Column(Modifier.weight(1f)) {
                    Spacer(Modifier.padding(vertical=5.dp))
                    TextBasicIcon(text = reservation.startTime+"-"+reservation.endTime, icon = Icons.Default.AccessTime)
                    Spacer(Modifier.padding(vertical=5.dp))
                }
            }

        }
    }

}