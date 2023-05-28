package com.example.mad.screens.profile

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.ButtonDialog
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.FloatingButtonAdd
import com.example.mad.common.composable.FullDialogSport
import com.example.mad.common.composable.IconButtonRating
import com.example.mad.common.composable.Score
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextBasicTitle
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.common.getSport
import com.example.mad.model.ProfileSport

//TODO() Dialog add/edit sport



@Composable
fun ProfileSportScreen(
    navController: NavHostController,
    vm: MainViewModel
) {

    val (isOpenDialog, openDialog) = remember {
        mutableStateOf(false)
    }

    val profileSport = remember {
        mutableStateOf<List<ProfileSport>>(emptyList())
    }
    val userId = vm.currentUser?.email?:""

    val allSport = getSport()
//    val filteredSport = remember {
//        mutableStateListOf<String>()
//    }

    vm.getAllUserProfileSport(userId).observe(LocalLifecycleOwner.current) {
        val sports = it.filterNotNull()
        profileSport.value = sports


//        allSport.forEach { sport->
//            if(!profileSport.value.map { pSport->pSport.sport }.contains(sport)){
//                filteredSport.add(sport)
//            }
//        }

    }

    fun goHome() {
        navController.navigate(BottomBarScreen.Home.route)
    }

    fun add() {
        //show Dialog
        openDialog(true)
    }




    if (isOpenDialog) {
        FullDialogSport(openDialog,vm,allSport)
    }

    val loading = vm.loadingProgressBar.value


    Scaffold(
        topBar = {
            TopBarBackButton(R.string.topBarUserSport, ::goHome)
        },
        floatingActionButton = { FloatingButtonAdd(::add) }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Achievements(
                vm,
                profileSport
            )
            CircularProgressBar(isDisplayed = loading)

        }
    }
}

@Composable
fun Achievements(
    vm: MainViewModel,
    profileSport: MutableState<List<ProfileSport>>

) {

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(profileSport.value) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                elevation = CardDefaults.cardElevation(),
                colors=CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant
                , contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
            ) {
                Row {

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(3f)
                    ) {
                        InfoSportCard(item)
                    }
                    Column(
                        Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = {
                            val userId = vm.currentUser?.email?:""
                            vm.deleteUserProfileSport(userId,item.sport)
                        }) {
                            Icon(Icons.Default.Delete,tint=MaterialTheme.colorScheme.error, contentDescription = "deleteButton")
                        }
                    }

                }
            }
        }
    }

}

@Composable
fun InfoSportCard(
    item: ProfileSport
) {
    val sport = item.sport
    val level = item.level
    val trophies = item.trophies

    TextBasicHeadLine(sport)
    Spacer(Modifier.padding(vertical = 10.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column {
            TextBasicTitle("Level ")
        }
        Column(Modifier.padding(horizontal = 10.dp)) {
            Score(level)
        }
    }
    Spacer(Modifier.padding(vertical = 5.dp))

    TextBasicTitle("Trophies $trophies")

}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewProfileSport() {
//    MadTheme {
//        ProfileSportScreen()
//    }
//}
//
//
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewProfileSportLandscape() {
//    MadTheme {
//        ProfileSportScreen()
//    }
//}
