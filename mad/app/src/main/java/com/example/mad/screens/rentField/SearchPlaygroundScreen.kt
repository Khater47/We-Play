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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CardPlayground
import com.example.mad.common.composable.DialogList
import com.example.mad.common.composable.TextBasicIcon
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.model.Playground
import com.example.mad.ui.theme.MadTheme


//TODO: change design of dialog list (m3)

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
            SearchPlaygroundContainer(vm)
        }
    }

}

@Composable
fun SearchPlaygroundContainer(
    vm:MainViewModel
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



    //Not filter
    if(sport.isEmpty() && location.isEmpty()){
        vm.getPlaygrounds().observe(lifecycleOwner){
            playgrounds.value=it.filterNotNull()
        }
    }
    //Filter by sport
    else if(sport.isNotBlank() && location.isEmpty()){

        vm.getPlaygroundsBySport(sport).observe(lifecycleOwner){
            playgrounds.value=it.filterNotNull()

        }
    }
    //Filter by location
    else if(sport.isEmpty() && location.isNotBlank()){

        vm.getPlaygroundsByLocation(location).observe(lifecycleOwner){
            playgrounds.value=it.filterNotNull()

        }
    }
    //Filter by sport and location
    else {
        vm.getPlaygroundsByLocationAndSport(sport, location).observe(lifecycleOwner){
            playgrounds.value=it.filterNotNull()
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
        LazyColumn {
            items(playgrounds.value) {item ->
                CardPlayground(item)
            }
        }

    val sportList = listOf(
        "Soccer",
        "Volleyball",
        "Basket",
        "Cricket",
    )
    val locationList = listOf(
        "Turin",
        "Milan",
        "Rome",
        "Venice",
    )

    if (isOpenSportDialog) {
        DialogList(sportList,openSportDialog,setSport)
        }
    if (isOpenLocationDialog) {
        DialogList(locationList,openLocationDialog,setLocation)
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
            )
            ,onClick = {
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
            )
            ,onClick = {
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
