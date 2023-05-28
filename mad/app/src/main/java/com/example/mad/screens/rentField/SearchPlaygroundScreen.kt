package com.example.mad.screens.rentField

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CardPlaygroundFullLocation
import com.example.mad.common.composable.DefaultImage
import com.example.mad.common.composable.DialogList
import com.example.mad.common.composable.InfoSearchPlayground
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextBasicIcon
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.common.getLocation
import com.example.mad.common.getSport
import com.example.mad.model.Playground



@Composable
fun SearchPlaygroundScreen(
    navController: NavHostController,
    vm: MainViewModel
) {

    fun goToPreviousPage() {
        navController.navigate(BottomBarScreen.Home.route)
    }



    Scaffold(
        topBar = {
            TopBarBackButton(id = R.string.topBarSearchPlayground, ::goToPreviousPage)
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SearchPlaygroundContainer(vm,navController)
        }
    }

}

@Composable
fun SearchPlaygroundContainer(
    vm: MainViewModel,
    navController: NavHostController,
) {

    val (sport, setSport) = remember {
        mutableStateOf("")
    }

    val (location, setLocation) = remember {
        mutableStateOf("")
    }

    val playgrounds = remember {
        mutableStateOf<List<Playground>>(emptyList())
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    val orientation = LocalConfiguration.current.orientation


    //Not filter
    if (sport.isEmpty() && location.isEmpty()) {
        vm.getPlaygrounds().observe(lifecycleOwner) {
            playgrounds.value = it.filterNotNull()
        }
    }
    //Filter by sport
    else if (sport.isNotBlank() && location.isEmpty()) {

        vm.getPlaygroundsBySport(sport).observe(lifecycleOwner) {
            playgrounds.value = it.filterNotNull()

        }
    }
    //Filter by location
    else if (sport.isEmpty() && location.isNotBlank()) {

        vm.getPlaygroundsByLocation(location).observe(lifecycleOwner) {
            playgrounds.value = it.filterNotNull()

        }
    }
    //Filter by sport and location
    else {
        vm.getPlaygroundsByLocationAndSport(sport, location).observe(lifecycleOwner) {
            playgrounds.value = it.filterNotNull()
        }
    }

    val (isOpenSportDialog, openSportDialog) = remember {
        mutableStateOf(false)
    }
    val (isOpenLocationDialog, openLocationDialog) = remember {
        mutableStateOf(false)
    }


    Column(Modifier.padding(horizontal = 10.dp)) {
        ButtonGroup(
            openLocationDialog,
            openSportDialog
        )
        when(orientation){
            Configuration.ORIENTATION_PORTRAIT->{
                LazyColumn {
                    items(playgrounds.value) { item ->
                        CardPlaygroundFullLocation(item,navController,item.id)
                    }
                }
            }
            else -> {
                LazyColumn{
                    items(playgrounds.value){
                        item ->
                        Row {
                            Column(Modifier.weight(1f)) {
                                CardPlaygroundLandscape(item,navController,item.id)
                            }
                            Column(Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Column(Modifier.padding(10.dp)) {
                                    InfoSearchPlayground(item.address+","+item.city,item.sport)
                                }
                            }
                        }
                    }
                }
            }
        }
    }






    val sportList = getSport()

    val locationList = getLocation()

    if (isOpenSportDialog) {
        DialogList(sportList, "Select Sport", openSportDialog, setSport)
    }
    if (isOpenLocationDialog) {
        DialogList(locationList, "Select Location", openLocationDialog, setLocation)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPlaygroundLandscape(playground: Playground,navController: NavHostController,playgroundId: String){
    val playgroundTitle = playground.playground
    val playgroundSport = playground.sport

    Card(
        onClick = {
            //ADD RATING
                  navController.navigate(BottomBarScreen.Playground.route+"/$playgroundId")
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
fun ButtonGroup(
    openLocationDialog: (Boolean) -> Unit,
    openSportDialog: (Boolean) -> Unit
) {

    val orientation = LocalConfiguration.current.orientation

    var verticalAlignment: Alignment.Vertical = Alignment.Top
    var horizontalAlignment: Arrangement.Horizontal = Arrangement.SpaceEvenly
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
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ), onClick = {
                //DateDialog
                openSportDialog(true)
            }
        ) {
            TextBasicIcon(text = "Sport", icon = Icons.Default.SportsCricket)
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ), onClick = {
                //DateDialog
                openLocationDialog(true)
            }
        ) {
            TextBasicIcon(text = "Location", icon = Icons.Default.LocationOn)
        }
    }

}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        SearchPlaygroundScreen()
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
