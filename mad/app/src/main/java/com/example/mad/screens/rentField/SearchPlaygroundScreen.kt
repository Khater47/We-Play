package com.example.mad.screens.rentField

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CardPlaygroundFullLocation
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.DefaultImage
import com.example.mad.common.composable.InfoSearchPlayground
import com.example.mad.common.composable.ListRadioButtonData
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TopBarComplete
import com.example.mad.common.getLocation
import com.example.mad.common.getSport
import com.example.mad.model.Playground


@Composable
fun SearchPlaygroundScreen(
    navController: NavHostController,
    vm: MainViewModel
) {
    val sports = getSport()
    val cities = getLocation()

    val sport = remember {
        mutableStateOf("")
    }

    val city = remember {
        mutableStateOf("")
    }

    val changeUi = remember {
        mutableStateOf(true)
    }

    val openDialog = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = changeUi.value) {
        if (changeUi.value) {
            vm.getPlaygrounds(sport.value, city.value)
            changeUi.value = false
        }
    }

    fun goToPreviousPage() {
        navController.navigate(BottomBarScreen.Home.route)
    }

    fun action() {
        openDialog.value = true
    }

    if (openDialog.value) {
        DialogFilter(openDialog, sport, city, sports, cities,changeUi)
    }

    val loading = vm.loadingProgressBar.value

    Scaffold(
        topBar = {
            TopBarComplete(
                id = R.string.topBarSearchPlayground, Icons.Default.FilterAlt,
                ::goToPreviousPage, ::action
            )
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SearchPlaygroundContainer(
                vm,
                navController,
                sport,
                city,
                changeUi
            )
            CircularProgressBar(isDisplayed = loading)
        }
    }

}

@Composable
fun SearchPlaygroundContainer(
    vm: MainViewModel,
    navController: NavHostController,
    sport:MutableState<String>,
    city:MutableState<String>,
    changeUi:MutableState<Boolean>
) {


    val playgrounds = vm.playgrounds.observeAsState().value?.filterNotNull() ?: emptyList()

    val orientation = LocalConfiguration.current.orientation

    Column(Modifier.padding(horizontal = 10.dp)) {

        if (sport.value!="" || city.value!="") {
            Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
                if(sport.value!=""){
                    AssistChip(onClick = { },
                        label = { Text(text = sport.value) },
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Close,
                                contentDescription = "Remove Filter",
                                modifier = Modifier
                                    .clickable(
                                        onClick = { sport.value=""
                                                changeUi.value=true
                                        }
                                    ))
                        })
                }
                if(city.value!="") {
                    AssistChip(onClick = { },modifier=Modifier.padding(start=10.dp),
                        label = { Text(text = city.value) },
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Close,
                                contentDescription = "Remove Filter",
                                modifier = Modifier
                                    .clickable(
                                        onClick = { city.value=""
                                            changeUi.value=true
                                        }
                                    ))
                        })
                }

            }

        }


        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                LazyColumn {
                    items(playgrounds) { item ->
                        CardPlaygroundFullLocation(
                            item,
                            navController,
                            item.address + " " + item.city
                        )
                    }
                }
            }

            else -> {
                LazyColumn {
                    items(playgrounds) { item ->
                        Row {
                            Column(Modifier.weight(1f)) {
                                CardPlaygroundLandscape(
                                    item,
                                    navController,
                                    item.address + " " + item.city
                                )
                            }
                            Column(
                                Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    InfoSearchPlayground(item.address + "," + item.city, item.sport)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPlaygroundLandscape(
    playground: Playground,
    navController: NavHostController,
    playgroundId: String
) {
    val playgroundTitle = playground.playground
    val playgroundSport = playground.sport

    Card(
        onClick = {
            navController.navigate(BottomBarScreen.Playground.route + "/$playgroundId")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Column(Modifier.padding(10.dp)) {
                TextBasicHeadLine(text = playgroundTitle)
            }
            DefaultImage(image = playgroundSport)
        }
    }
}


@Composable
fun DialogFilter(
    openDialog: MutableState<Boolean>,
    sport:MutableState<String>,
    city:MutableState<String>,
    sports: List<String>,
    cities: List<String>,
    changeUi:MutableState<Boolean>
) {

    val selectedSport = remember {
        mutableStateOf(-1)
    }

    val selectedCity = remember {
        mutableStateOf(-1)
    }

    Dialog(
        onDismissRequest = {
            openDialog.value = false
        },
    ) {
        Card(shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )){
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {

                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Select Sport", fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(15.dp)
                    )
                    Divider(Modifier.padding(bottom = 10.dp))
                    ListRadioButtonData(
                        data = sports, state = selectedSport,
                        modifier = Modifier.height(150.dp)
                    )
                    Divider(Modifier.padding(bottom = 10.dp))

                }

                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Select Sport", fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(15.dp)
                    )
                    Divider(Modifier.padding(vertical = 10.dp))
                    ListRadioButtonData(
                        data = cities, state = selectedCity,
                        modifier = Modifier.height(150.dp)
                    )
                    Divider(Modifier.padding(bottom = 10.dp))

                }

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            openDialog.value = false
                        }, modifier = Modifier.padding(horizontal = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            backgroundColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "Cancel", fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Button(
                        onClick = {
                            val s = if(selectedSport.value==-1) {""} else sports[selectedSport.value]
                            val c = if(selectedCity.value==-1) {""} else cities[selectedCity.value]
                            sport.value=s
                            city.value=c

                            changeUi.value=true
                            openDialog.value = false


                        }, modifier = Modifier.padding(horizontal = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            backgroundColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "Save", fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

    }

}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
////    val openDialog = remember {
////        mutableStateOf(true)
////    }
//    MadTheme {
//        DialogFilter(/*openDialog*/)
//    }
//}
//
//
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewLandscape() {
//    MadTheme {
//        SearchPlaygroundScreen()
//    }
//}
