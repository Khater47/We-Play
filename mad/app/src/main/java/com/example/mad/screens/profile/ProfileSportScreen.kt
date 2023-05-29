package com.example.mad.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.FloatingButtonAdd
import com.example.mad.common.composable.FullDialogSport
import com.example.mad.common.composable.FullDialogSportEdit
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

    val (isOpenDialogEdit, openDialogEdit) = remember {
        mutableStateOf(false)
    }

    val (isSport, sport) = remember {
        mutableStateOf("")
    }

    val (isTrophies, trophies) = remember {
        mutableStateOf(0)
    }
    val (isLevel, level) = remember {
        mutableStateOf(0)
    }

//    val profileSport = remember {
//        mutableStateOf<List<ProfileSport>>(emptyList())
//    }

    val profileSport = vm.userSports.observeAsState().value?.filterNotNull()?: emptyList()

    val userId = vm.currentUser?.email?:""

    LaunchedEffect(key1 = null){
        vm.getAllUserProfileSport(userId)
    }

    val addSports = remember {
        mutableStateOf<List<String>>(emptyList())
    }

    if(profileSport.isNotEmpty()){
        val allSport = getSport().toMutableList()

        allSport.removeAll{it in profileSport.map { p -> p.sport }}

        addSports.value = allSport
    }

    fun goHome() {
        navController.navigate(BottomBarScreen.Home.route)
    }

    fun add() {
        //show Dialog
        openDialog(true)
    }

    fun edit(s: String, l: Int, t: Int) {
        //show dialog Edit
        openDialogEdit(true)
        sport(s)
        trophies(t)
        level(l)
    }


    if (isOpenDialog) {
        FullDialogSport(openDialog,vm,addSports.value)
    }

    if (isOpenDialogEdit) {
        FullDialogSportEdit(openDialogEdit, vm,isSport,isLevel,isTrophies)
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
                profileSport,
                ::edit
            )
            CircularProgressBar(isDisplayed = loading)

        }
    }
}

@Composable
fun Achievements(
    vm: MainViewModel,
    profileSport: List<ProfileSport>,
    action: (sport:String,level:Int,trophies:Int)->Unit
) {

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(profileSport) { item ->
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
                        Row() {
                            IconButton(onClick = {
                                val userId = vm.currentUser?.email?:""
                                vm.deleteUserProfileSport(userId,item.sport)
                            }) {
                                Icon(Icons.Default.Delete,tint=MaterialTheme.colorScheme.error, contentDescription = "deleteButton")
                            }
                        }
                        Row() {
                            IconButton(onClick = {
                                action(item.sport, item.level.toInt(), item.trophies.toInt())

                            }) {
                                Icon(Icons.Default.Edit,tint=MaterialTheme.colorScheme.primary, contentDescription = "editButton")
                            }
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
