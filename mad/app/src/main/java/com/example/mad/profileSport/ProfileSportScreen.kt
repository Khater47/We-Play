package com.example.mad.profileSport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mad.model.ProfileSport
import com.example.mad.ui.theme.MadTheme
import com.example.mad.utils.convertAchievement
import com.example.mad.utils.getColorListFromLevel



@Composable
fun ProfileSportScreen(
//     navController: NavHostController,
//    vm:UserViewModel
) {

//    val userId="2"
//    val lifecycleOwner = LocalLifecycleOwner.current
//
    val profileSport1 = ProfileSport(0, "first", "soccer", 3, 1)
    val profileSport2 = ProfileSport(0, "second", "baseball", 5, 1)
    val profileSport3 = ProfileSport(0, "third", "football", 0, 1)
    val profileSportList = remember { mutableStateListOf<ProfileSport>() }

    profileSportList.addAll(listOf(profileSport1, profileSport2, profileSport3))
//
//    vm.sports.observe(lifecycleOwner) { sports ->
//        sports.forEach{ sport->
//            val p = ProfileSport(0,"",sport,0,userId.toInt())
//            profileSportList.add(p)
//        }
//
//    }


    Scaffold(
        topBar = {
            TopAppBarSport(
//        navController
            )
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            Achievements(profileSportList)
        }
    }


}

@Composable
fun TopAppBarSport(
// navController: NavHostController
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
//                navController.navigate(BottomBarScreen.Home.route)
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {},
        elevation = 10.dp

    )
}



@Composable
fun DialogAddUserProfileSport(sport:String,userId:String,setShowDialogSport:(Boolean)->Unit){

    val selectedAchievement = remember { mutableStateListOf(false, false, false) }

    var selectedLevel by remember {
        mutableStateOf(0)
    }

    Dialog(onDismissRequest = { setShowDialogSport(false) }) {
        Card(shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp,Color.LightGray),
            colors = CardDefaults.cardColors()
        ) {
            Column {
                Text(
                    "Choose Level and Achievement for $sport",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 10.dp)
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

                Row(Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                    ){
                    Button(onClick = {
                        setShowDialogSport(false)
                    },
                        shape= CircleShape,
                        modifier=Modifier.weight(1f)
                        ) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {

                        val achievement = convertAchievement(selectedAchievement.indexOf(true))
                        val level = if(selectedLevel==0) null else selectedLevel
                        val p = ProfileSport(0,achievement,sport,level,userId.toInt())

                        //viewModel add user profile sport


                        setShowDialogSport(false)

                    },
                        shape= CircleShape,
                        modifier=Modifier.weight(1f)

                    ) {
                        Text(text = "Confirm")
                    }

                }
            }

        }



    }
}

@Composable
fun Achievements(profileSportList: List<ProfileSport>) {

    val (showDialogSport, setShowDialogSport) = remember { mutableStateOf(false) }

    var sportDialog by remember {
        mutableStateOf("")
    }

    //Dialog for edit level and achievement for selected sport
    if (showDialogSport) {
        DialogAddUserProfileSport(sport = sportDialog,
            userId="2",
            setShowDialogSport = setShowDialogSport
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
                        Text(text = item.sport, style = MaterialTheme.typography.h5)

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
                            } else {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = Color(0xFFCD7F32)
                                )
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
                            //TODO() delete
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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ProfileSportScreen()
    }
}
