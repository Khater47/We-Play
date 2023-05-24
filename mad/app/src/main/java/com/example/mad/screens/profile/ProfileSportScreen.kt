package com.example.mad.screens.profile

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.ButtonDialog
import com.example.mad.common.composable.FloatingButtonAdd
import com.example.mad.common.composable.FullDialogSport
import com.example.mad.common.composable.IconButtonDelete
import com.example.mad.common.composable.IconButtonRating
import com.example.mad.common.composable.Score
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextBasicTitle
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.model.ProfileSport
import com.example.mad.ui.theme.Bronze
import com.example.mad.ui.theme.Gold
import com.example.mad.ui.theme.MadTheme
import com.example.mad.ui.theme.Silver

//TODO() Dialog add/edit sport

@Composable
fun SelectSportDropDownMenu(
    sportsList: List<String>,
    selectedSport: String,
    setSelectedSport: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    val icon = if (expanded) {
        Icons.Default.KeyboardArrowUp
    } else {
        Icons.Default.KeyboardArrowDown
    }

    Column(modifier = Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = selectedSport,
            onValueChange = {
                setSelectedSport(it)
            }, modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates -> textFieldSize = coordinates.size.toSize() },
            readOnly = true,
            label = {
                Text(text = "Select Sport")
            },
            trailingIcon = {
                Icon(icon, contentDescription = "", Modifier.clickable { expanded = !expanded })
            })

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            sportsList.forEach { label ->
                DropdownMenuItem(onClick = {
                    setSelectedSport(label)
                    expanded = false
                }) {
                    Text(text = label.replaceFirstChar { it.uppercase() })
                }
            }
        }
    }
}


@Composable
fun SportDialog(
    openDialog: (Boolean) -> Unit,
    sportsList: List<String>,
    selectedSport: String,
    setSelectedSport: (String) -> Unit,
    level: Int,
    trophies: Int,
    setLevel: (Int) -> Unit,
    setTrophies: (Int) -> Unit,
) {

    Dialog(onDismissRequest = { openDialog(false) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors()
        ) {
            Column(Modifier.clip(RoundedCornerShape(10.dp))) {

                SelectSportDropDownMenu(sportsList, selectedSport, setSelectedSport)

                SportStatDialog(level, trophies, setLevel, setTrophies)
                
                Spacer(modifier = Modifier.padding(vertical=10.dp))

                ButtonDialog(cancel = { openDialog(false) }, confirm = {
                    //viewModel.insertUserSport
                    Log.d("TAG", "$selectedSport $level $trophies")
                })
            }
        }

    }

}

@Composable
fun SportStatDialog(
    level: Int,
    trophies: Int,
    setLevel: (Int) -> Unit,
    setTrophies: (Int) -> Unit,
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Level", fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium)
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            IconButtonRating(level, setLevel)
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Trophies", fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ProfileSportScreen(
    navController: NavHostController,
    vm: MainViewModel
) {

//    val sportsList = listOf<String>(
//        "soccer","volleyball","cricket","basket","baseball"
//    )

//    val (selectedSport, setSelectedSport) = remember {
//        mutableStateOf("")
//    }
    val (isOpenDialog, openDialog) = remember {
        mutableStateOf(false)
    }



    val profileSport = remember {
        mutableStateOf<List<ProfileSport>>(emptyList())
    }
    val userId = "f9SYx0LJM3TSDxUFMcX6JEwcaxh1"

    vm.getAllUserProfileSport(userId).observe(LocalLifecycleOwner.current) {
        val sports = it.filterNotNull()
        profileSport.value = sports
    }

    fun goHome() {
        navController.navigate(BottomBarScreen.Home.route)
    }

    fun add() {
        //show Dialog
        openDialog(true)
    }



    if (isOpenDialog) {
        FullDialogSport(openDialog,vm)
    }


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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Achievements(
    vm: MainViewModel,
    profileSport: MutableState<List<ProfileSport>>

) {

    fun delete() {

    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(profileSport.value) { item ->
            Card(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                elevation = CardDefaults.cardElevation(),
                colors=CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface
                , contentColor = MaterialTheme.colorScheme.onSurface),
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
                        IconButtonDelete(::delete)
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
