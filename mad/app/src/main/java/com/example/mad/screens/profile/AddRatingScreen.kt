package com.example.mad.screens.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mad.MainViewModel
import com.example.mad.common.composable.IconButtonRating
import com.example.mad.common.getIconPlayground
import com.example.mad.common.getIconSport
import com.example.mad.model.PlaygroundRating
import com.example.mad.model.ProfileRating
import com.example.mad.model.Reservation


@Composable
fun FullDialogAddRating(
    openDialog: MutableState<Boolean>,
    reservation: Reservation,
    vm: MainViewModel,
    changeUi:MutableState<Boolean>
) {
    val (quality, setQuality) = remember {
        mutableStateOf(0)
    }
    val (facilities, setFacilities) = remember {
        mutableStateOf(0)
    }

    val nickname = vm.profile.observeAsState().value?.nickname?:""

    LaunchedEffect(key1 = null){
        vm.getUserProfile()
    }

    val text = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    fun addRating() {
        if(nickname.isNotEmpty()){
            val id = reservation.address+" "+reservation.city
            val userRating = ProfileRating(
                reservation.address,
                reservation.city,
                reservation.playground
            )
            val playgroundRating = PlaygroundRating(
                quality.toLong(),
                facilities.toLong(),
                text.value,
                nickname
            )
            vm.insertUserRating(userRating)
            vm.insertUserPlaygroundRating(id,playgroundRating)
            changeUi.value=true
        }
        else {
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
        }
        openDialog.value=false
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
                )
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
                            imageVector = Icons.Default.Clear,
                            contentDescription = "close dialog",
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
                        text = "Rate your playground",
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium
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
                            addRating()
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

            Column {
                /*RESERVATION INFO*/
                Column(Modifier.padding(vertical = 30.dp)) {
                    /*PLAYGROUND*/
                    Column(Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)) {
                        Text(
                            text = reservation.playground,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                        )
                    }

                    Image(
                        painter = painterResource(id = getIconPlayground(reservation.sport)),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                    )

                    /*ADDRESS, CITY*/
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 15.dp)
                    ) {
                        TextIcon(
                            text = reservation.address + " " + reservation.city,
                            icon = Icons.Default.LocationOn
                        )
                    }

                    /*SPORT*/
                    Column(Modifier.padding(vertical = 20.dp)) {
                        TextIcon(text = reservation.sport, icon = getIconSport(reservation.sport))
                    }

                }

                //QUALITY
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Column(
                        Modifier.weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Quality",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(Modifier.weight(4f)) {
                        IconButtonRating(score = quality, setScore = setQuality)
                    }
                }

                //FACILITIES
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        Modifier.weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Facilities",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(Modifier.weight(4f)) {
                        IconButtonRating(score = facilities, setScore = setFacilities)
                    }

                }

            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 5.dp)
            ) {

                OutlinedTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    label = { Text("Comment Section") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(200.dp)
                        .background(Color.White)
                )


            }


        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewAddRating() {
//    MadTheme {
//        FullDialogEditReservation()
//    }
//}

//
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewAddRatingLandscape() {
//    MadTheme {
//        RatingTextSection(text = "")
//    }
//
//}
