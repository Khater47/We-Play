package com.example.mad.profileSport

//import androidx.compose.ui.tooling.preview.Preview
//import com.example.mad.ui.theme.MadTheme
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
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.mad.utils.convertAchievement
import com.example.mad.utils.getColorListFromLevel


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

    val sports = remember {
        mutableStateListOf<String>()
    }


    val profileSportList = remember { mutableStateListOf<ProfileSport>() }


    // Get the list of available sports from db
    vm.sports.observe(lifecycleOwner) { sportsList ->
        sportsList.forEach {
            sports.add(it)
        }
    }

    // Get the user Profile Sports from the db
    //Note: fix this method later because it runs more tha  once
    vm.getProfileSportByIdProfile(userId.toInt()).observe(lifecycleOwner) { profileSports ->
        profileSports.forEach {
            if (profileSportList.find { profileSport -> profileSport.id == it.id } == null) {
                profileSportList.add(it)
            }
        }
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
                containerColor = Color(0xFF6750A4)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(0xFFFFFFFF)
                )
            }
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            Achievements(vm, profileSportList, showDialogAddSport, setShowDialogAddSport, sports)
        }
    }


}

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
    vm: UserViewModel,
    userId: String,
    setShowDialogAddSport: (Boolean) -> Unit,
    sportsList: List<String>,
    profileSportList: MutableList<ProfileSport>
) {

    val selectedAchievement = remember { mutableStateListOf(false, false, false) }

    val context = LocalContext.current

    var selectedLevel by remember {
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
            Column {
                SelectSportDropDownMenu(sportsList, selectedSport, setSelectedSport)
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { selectedLevel = 1 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 1) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = { selectedLevel = 2 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 2) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = { selectedLevel = 3 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 3) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = { selectedLevel = 4 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 4) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = { selectedLevel = 5 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 5) Color(0xFFFFB600) else Color.Gray
                        )
                    }


                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        selectedAchievement[0] = true
                        selectedAchievement[1] = false
                        selectedAchievement[2] = false
                    }) {

                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = if (selectedAchievement[0]) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = {
                        selectedAchievement[0] = false
                        selectedAchievement[1] = true
                        selectedAchievement[2] = false
                    }) {

                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = if (selectedAchievement[1]) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = {
                        selectedAchievement[0] = false
                        selectedAchievement[1] = false
                        selectedAchievement[2] = true
                    }) {

                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = if (selectedAchievement[2]) Color(0xFFFFB600) else Color.Gray
                        )
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            setShowDialogAddSport(false)
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCF142B))
                    ) {
                        Text(text = "Cancel", color = Color.White)
                    }
                    Button(
                        onClick = {

                            val achievement = convertAchievement(selectedAchievement.indexOf(true))
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
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
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

    val selectedAchievement = remember { mutableStateListOf(false, false, false) }

    var selectedLevel by remember {
        mutableStateOf(0)
    }

    Dialog(onDismissRequest = { setShowDialogSport(false) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors()
        ) {
            Column {
                Text(
                    "Edit ${sport.replaceFirstChar { it.uppercase() }} card",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 10.dp),
                    textAlign = TextAlign.Center
                )

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { selectedLevel = 1 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 1) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = { selectedLevel = 2 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 2) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = { selectedLevel = 3 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 3) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = { selectedLevel = 4 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 4) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = { selectedLevel = 5 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (selectedLevel >= 5) Color(0xFFFFB600) else Color.Gray
                        )
                    }


                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        selectedAchievement[0] = true
                        selectedAchievement[1] = false
                        selectedAchievement[2] = false
                    }) {

                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = if (selectedAchievement[0]) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = {
                        selectedAchievement[0] = false
                        selectedAchievement[1] = true
                        selectedAchievement[2] = false
                    }) {

                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = if (selectedAchievement[1]) Color(0xFFFFB600) else Color.Gray
                        )
                    }

                    IconButton(onClick = {
                        selectedAchievement[0] = false
                        selectedAchievement[1] = false
                        selectedAchievement[2] = true
                    }) {

                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = if (selectedAchievement[2]) Color(0xFFFFB600) else Color.Gray
                        )
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            setShowDialogSport(false)
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCF142B))
                    ) {
                        Text(text = "Cancel", color = Color.White)
                    }
                    Button(
                        onClick = {

                            val achievement = convertAchievement(selectedAchievement.indexOf(true))
                            val level = if (selectedLevel == 0) null else selectedLevel
                            val p = ProfileSport(
                                profileSportId,
                                achievement,
                                sport,
                                level,
                                userId.toInt()
                            )

                            //viewModel Update user profile sport
                            vm.updateProfileSport(p)
                            val index =
                                profileSportList.indexOfFirst { profileSport -> profileSport.id == profileSportId }
                            profileSportList[index] = p

                            setShowDialogSport(false)

                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF32CD32))

                    ) {
                        Text(
                            text = "Confirm",
                            color = Color.White,
                        )
                    }

                }
            }

        }


    }
}

@Composable
fun Achievements(
    vm: UserViewModel,
    profileSportList: MutableList<ProfileSport>,
    showDialogAddSport: Boolean,
    setShowDialogAddSport: (Boolean) -> Unit,
    sportsList: List<String>
) {

    val (showDialogSport, setShowDialogSport) = remember { mutableStateOf(false) }

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

    if (showDialogAddSport) {
        DialogAddUserProfileSport(
            vm,
            userId = "2",
            setShowDialogAddSport = setShowDialogAddSport,
            sportsList,
            profileSportList
        )
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(profileSportList, itemContent = { item ->

            val levelListColor = getColorListFromLevel(item.level ?: 0)
            val achievement = item.achievement as String

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
                        Text(
                            text = item.sport.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.h5
                        )

                        Row(
                            Modifier.padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Level: ", modifier = Modifier.padding(end = 2.dp))

                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (levelListColor[0] == 0) Color.Gray else Color(0xFFFFB600)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (levelListColor[1] == 0) Color.Gray else Color(0xFFFFB600)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (levelListColor[2] == 0) Color.Gray else Color(0xFFFFB600)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (levelListColor[3] == 0) Color.Gray else Color(0xFFFFB600)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (levelListColor[4] == 0) Color.Gray else Color(0xFFFFB600)
                            )

                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (achievement != "null") {
                                Text(text = "Achievement: ")
                                if (achievement.contains("first")) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = Color(0xFFFFB600)
                                    )
                                } else if (achievement.contains("second")) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                } else if (achievement.contains("third")) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = Color(0xFFCD7F32)
                                    )
                                }
                            }

                        }

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
                }

            }

        })
    }


}


/*@Preview(showBackground = true, name = "User profile Sport")
@Composable
fun DefaultPreview() {
    MadTheme() {
        DialogEditUserProfileSport(sport = "football", userId = "2", setShowDialogSport = { true })
    }
}*/
