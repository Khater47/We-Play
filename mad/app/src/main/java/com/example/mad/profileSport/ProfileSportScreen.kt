package com.example.mad.profileSport

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import com.example.mad.activity.BottomBarScreen
import com.example.mad.model.ProfileSport
import com.example.mad.ui.theme.bronze
import com.example.mad.ui.theme.gold
import com.example.mad.utils.convertAchievement
import com.example.mad.utils.getColorFromAchievement


@Composable
fun TopAppBarSport(
    navController: NavHostController
) {


    TopAppBar(
        title = {
            Text(
                text = "User Profile Sport",
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(BottomBarScreen.Home.route)
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {},
        elevation = 10.dp

    )
}

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
fun DialogAddUserProfileSport(
    userId: String,
    setShowDialogAddSport: (Boolean) -> Unit,
    sportsList: List<String>,
    profileSportList: MutableList<ProfileSport>,
    vm: UserViewModel,
) {
    val context = LocalContext.current

    val (selectedLevel, setSelectedLevel) = remember {
        mutableStateOf(0)
    }
    val (selectedAchievement, setSelectedAchievement) = remember {
        mutableStateOf(0)
    }

    val (selectedSport, setSelectedSport) = remember {
        mutableStateOf("")
    }


    Dialog(onDismissRequest = { setShowDialogAddSport(false) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SelectSportDropDownMenu(sportsList, selectedSport, setSelectedSport)
            }

            DialogLevelAndAchievementButton(selectedLevel, setSelectedLevel, setSelectedAchievement)


            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    ButtonCancel(setShowDialogAddSport)
                }

                Column(
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val achievement = convertAchievement(selectedAchievement)
                            val level = if (selectedLevel == 0) null else selectedLevel
                            val p =
                                ProfileSport(0, achievement, selectedSport, level, userId.toInt())


                            //viewModel add user profile sport
                            if (p.sport != "") {

                                if (profileSportList.find { profileSport -> profileSport.sport == p.sport } == null) {

                                    vm.insertProfileSport(p)
                                    setShowDialogAddSport(false)

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Sport already exists!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please select a sport",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF32CD32))

                    ) {
                        Text(text = "Confirm", color = Color.White)
                    }
                }

            }

        }


    }
}

@Composable
fun DialogEditUserProfileSport(
    vm: UserViewModel,
    sport: String,
    profileSportId: Int,
    userId: String,
    setShowDialogSport: (Boolean) -> Unit,
    profileSportList: MutableList<ProfileSport>
) {


    val (selectedLevel, setSelectedLevel) = remember {
        mutableStateOf(0)
    }
    val (selectedAchievement, setSelectedAchievement) = remember {
        mutableStateOf(0)
    }


    Dialog(onDismissRequest = { setShowDialogSport(false) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "Edit ${sport.replaceFirstChar { it.uppercase() }} results",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 10.dp),
                    textAlign = TextAlign.Center
                )
            }

            DialogLevelAndAchievementButton(selectedLevel, setSelectedLevel, setSelectedAchievement)


            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    ButtonCancel(setShowDialogSport)
                }

                Column(
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val achievement = convertAchievement(selectedAchievement)
                            val level = if (selectedLevel == 0) null else selectedLevel
                            val p = ProfileSport(
                                profileSportId,
                                achievement,
                                sport,
                                level,
                                userId.toInt()
                            )

//                            viewModel Update user profile sport
                            vm.updateProfileSport(p)
                            val index =
                                profileSportList.indexOfFirst { profileSport -> profileSport.id == profileSportId }
                            profileSportList[index] = p


                            setShowDialogSport(false)
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF32CD32))

                    ) {
                        Text(text = "Confirm", color = Color.White)
                    }
                }

            }

        }


    }
}


@Composable
fun DialogLevelAndAchievementButton(
    selectedLevel: Int,
    setSelectedLevel: (Int) -> Unit,
    setSelectedAchievement: (Int) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
    ) {
        ButtonLevelDialog(selectedLevel, setSelectedLevel)

        ButtonAchievementDialog(setSelectedAchievement)
    }

}


@Composable
fun ButtonCancel(showDialog: (Boolean) -> Unit) {
    Button(
        onClick = {
            showDialog(false)
        },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCF142B))
    ) {
        Text(text = "Cancel", color = Color.White)
    }
}

@Composable
fun ProfileSportScreen(
    navController: NavHostController,
    vm: UserViewModel
) {

    val userId = "2"
    val lifecycleOwner = LocalLifecycleOwner.current


    val (showDialogAddSport, setShowDialogAddSport) = remember {
        mutableStateOf(false)
    }

    // Get the list of available sports from db
    val sportsList = vm.sports.observeAsState().value?: emptyList()

    val profileSportList = remember { mutableStateListOf<ProfileSport>() }

    // Get the user Profile Sports from the db
    //Note: fix this method later because it runs more tha  once
    vm.getProfileSportByIdProfile(userId.toInt()).observe(lifecycleOwner) { profileSports ->
        profileSports.forEach {
            if (profileSportList.find { profileSport -> profileSport.id == it.id } == null) {
                profileSportList.add(it)
            }
        }
    }

//    val profileSportList = mutableListOf(
//        ProfileSport(1, "first", "soccer", 5, 1),
//        ProfileSport(2, "third", "baseball", 2, 1),
//    )
//    val sportsList = listOf("cricket","volleyball")

    if (showDialogAddSport) {
        DialogAddUserProfileSport(
            userId = "2",
            setShowDialogAddSport,
            sportsList,
            profileSportList,
            vm,
        )
    }


    Scaffold(
        topBar = {
            TopAppBarSport(
                navController
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    setShowDialogAddSport(true)
                },
                containerColor = Color(0xFF6750A4),
                modifier = Modifier.clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            Achievements(
                vm,
                profileSportList,
            )
        }
    }


}


@Composable
fun Achievements(
    vm: UserViewModel,
    profileSportList: MutableList<ProfileSport>,
) {

    val (showDialogSport, setShowDialogSport) = remember { mutableStateOf(false) }
//
    var sportDialog by remember {
        mutableStateOf("")
    }

    var profileSportId by remember {
        mutableStateOf(-1)
    }

    //Dialog for edit level and achievement for selected sport
    if (showDialogSport) {
        DialogEditUserProfileSport(
            vm,
            sport = sportDialog,
            profileSportId = profileSportId,
            userId = "2",
            setShowDialogSport = setShowDialogSport,
            profileSportList
        )
    }


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(profileSportList, itemContent = { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        setShowDialogSport(true)
                        sportDialog = item.sport
                        profileSportId = item.id
                    },
                elevation = CardDefaults.cardElevation(),
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

                        ButtonDeleteCard(vm,profileSportList,item)
                    }
                }

            }

        })
    }


}

@Composable
fun ButtonDeleteCard(vm:UserViewModel,profileSportList:MutableList<ProfileSport>,item:ProfileSport) {
    IconButton(onClick = {
        vm.deleteProfileSport(item)
        profileSportList.remove(item)
    }, Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.Red,
        )
    }
}

@Composable
fun InfoSportCard(item: ProfileSport) {

    Text(
        text = item.sport.replaceFirstChar { it.uppercase() },
        style = MaterialTheme.typography.h5
    )

    if (item.level != null) {
        Row(
            Modifier.padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Level ", modifier = Modifier.padding(end = 2.dp))

            LevelRow(item.level)

        }
    }

    if (item.achievement != "null" && item.achievement != null) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(text = "Achievement ")

            AchievementIcon(color = getColorFromAchievement(item.achievement))

        }
    }


}

@Composable
fun LevelRow(level: Int) {

    if (level != 0) {
        LazyRow {
            items(5) { index ->

                if (index < level)
                    LevelIcon(gold)
                else
                    LevelIcon(Color.Gray)

            }
        }
    }
}

@Composable
fun ButtonLevelDialog(selectedLevel: Int, setSelectedLevel: (Int) -> Unit) {

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items(5) { index ->
            IconButton(onClick = {
                //selectedScore
                setSelectedLevel(index + 1)
            }) {
                if (index < selectedLevel)
                    LevelIcon(gold)
                else
                    LevelIcon(Color.Gray)

            }
        }
    }
}

@Composable
fun ButtonAchievementDialog(setSelectedAchievement: (Int) -> Unit) {


    val selected = remember {
        mutableStateOf(listOf(false, false, false))
    }

    fun selectOne(index: Int) {

        val l = mutableListOf(false, false, false)
        l[index] = true

        selected.value = l.toList()

    }

    fun changeColor(index: Int): Color {
        return when (index + 1) {
            1 -> {
                gold
            }

            2 -> {
                Color.DarkGray
            }

            else -> {
                bronze
            }
        }
    }

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items(3) { index ->
            IconButton(onClick = {
                setSelectedAchievement(index + 1)
                selectOne(index)
            }) {
                AchievementIcon(if (!selected.value[index]) Color.Gray else changeColor(index))
            }

        }
    }
}

@Composable
fun LevelIcon(color: Color) {
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = null,
        tint = color
    )
}

@Composable
fun AchievementIcon(color: Color) {
    Icon(
        imageVector = Icons.Default.EmojiEvents,
        contentDescription = null,
        tint = color
    )
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        ProfileSportScreen()
//    }
//}
