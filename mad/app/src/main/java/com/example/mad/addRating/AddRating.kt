package com.example.mad.addRating

import android.content.res.Configuration
import android.graphics.Bitmap.Config
import android.widget.Toast
import androidx.compose.material.Card
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad.ui.theme.MadTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import com.example.mad.model.Playgrounds
import com.example.mad.utils.getIconPlayground
import com.example.mad.utils.getIconSport
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.platform.LocalConfiguration
import com.example.mad.activity.BottomBarScreen
import com.example.mad.model.ProfileRating


@Composable
fun AddRatingScreen(navController: NavHostController, vm: UserViewModel) {

    val (profileRating, setProfileRating) = remember {
        mutableStateOf<ProfileRating?>(null)
    }

    Scaffold(topBar = { TopAppBarAddRating(navController, vm, profileRating) }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            AddContainer(vm, setProfileRating)
        }
    }
}


@Composable
fun TopAppBarAddRating(
    navController: NavHostController, vm: UserViewModel, profileRating: ProfileRating?
) {

    val context = LocalContext.current

    TopAppBar(title = {
        Text(
            text = BottomBarScreen.AddRating.title,
            fontSize = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }, navigationIcon = {
        IconButton(onClick = {
            navController.navigate(BottomBarScreen.ProfileRating.route)
        }) {
            Icon(Icons.Filled.ArrowBack, "backIcon")
        }
    }, actions = {
        IconButton(onClick = {
            if (profileRating != null) {
                vm.insertProfileRating(profileRating)
                navController.navigate(BottomBarScreen.ProfileRating.route)
            } else {
                Toast.makeText(context, "Profile Rating not valid", Toast.LENGTH_SHORT).show()
            }
        }) {
            Icon(Icons.Filled.Check, "backIcon", Modifier.size(28.dp))
        }
    }, elevation = 10.dp

    )
}

@Composable
fun DialogSetRating(
    vm: UserViewModel, setPlayground: (Playgrounds?) -> Unit, setShowDialog: (Boolean) -> Unit
) {

    val playgrounds = vm.playgrounds.observeAsState().value ?: emptyList()

    val context = LocalContext.current

    var selected by remember { mutableStateOf<Playgrounds?>(null) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Card(
            modifier = Modifier.padding(10.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                Modifier
                    .width(350.dp)
                    .height(300.dp)
            ) {
                LazyColumn(Modifier.weight(3f)) {
                    items(playgrounds, itemContent = { item ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(color = if (selected?.id == item.id) Color.Gray else Color.White)
                        ) {
                            Text(
                                text = item.playground,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        selected = item
                                    },
                                fontSize = 20.sp
                            )

                            Divider()
                        }
                    })
                }

                Row(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            setShowDialog(false)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                        shape = CircleShape,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp),
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = {

                            setPlayground(selected)
                            setShowDialog(false)


                        }, shape = CircleShape, modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp)

                    ) {
                        Text(text = "Confirm")
                    }

                }
            }


        }


    }
}


@Composable
fun AddContainer(vm: UserViewModel, setProfileRating: (ProfileRating) -> Unit) {

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    val (playground, setPlayground) = remember { mutableStateOf<Playgrounds?>(null) }

    val (scoreQuality, setScoreQuality) = remember { mutableStateOf(0) }
    val (scoreFacilities, setScoreFacilities) = remember { mutableStateOf(0) }

    val orientation = LocalConfiguration.current.orientation

    if (showDialog) {
        DialogSetRating(vm, setPlayground, setShowDialog)
    }

    if (playground != null) {
        val ratingPlaygrounds =
            vm.getProfileRatingByIdProfile(2).observeAsState().value?.firstOrNull {
                it.idProfile == 2 && it.idPlayground == playground.id
            }
        val id = ratingPlaygrounds?.id ?: 0

        val p = ProfileRating(id, scoreQuality, scoreFacilities, 2, playground.id)
        setProfileRating(p)
    }

    Column(Modifier.verticalScroll(rememberScrollState())) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                setShowDialog(true)
            }) {
                Text("Select Playground", fontSize = 20.sp)
            }
        }

        if (playground != null) {

            val playgroundName = playground.playground
            val playgroundSport = playground.sport
            val playgroundLocation = playground.location

            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {

                        CardPlaygroundRating(
                            playgroundName,
                            playgroundSport,
                            playgroundLocation
                        )

                        RatingRow(
                            scoreQuality,
                            setScoreQuality,
                            "Quality"

                        )

                        RatingRow(
                            scoreFacilities,
                            setScoreFacilities,
                            "Facilities"
                        )
                    }
                }

                else -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {

                        Column(Modifier.weight(1f)) {

                            CardPlaygroundRating(
                                playgroundName,
                                playgroundSport,
                                playgroundLocation
                            )
                        }
                        Column(
                            Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                            RatingRow(
                                scoreQuality,
                                setScoreQuality,
                                "Quality"
                            )

                            RatingRow(
                                scoreFacilities,
                                setScoreFacilities,
                                "Facilities"

                            )
                        }
                    }
                }
            }


        }
    }


}

@Composable
fun CardPlaygroundRating(
    playgroundName: String, playgroundSport: String, playgroundLocation: String
) {

    val orientation = LocalConfiguration.current.orientation

    val heightImage =
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            200.dp
        } else {
            140.dp
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = 16.dp,
        shape = RoundedCornerShape(16.dp),
    ) {

        Column {
            Text(text = playgroundName, modifier = Modifier.padding(10.dp), fontSize = 20.sp)

            Image(
                painter = painterResource(id = getIconPlayground(playgroundSport)),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(heightImage)
                    .fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "IconLocation",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = playgroundLocation, modifier = Modifier.padding(10.dp)
                        )
                    }

                }
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = getIconSport(playgroundSport),
                            contentDescription = "IconLocation",
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = playgroundSport, modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }


        }


    }
}

@Composable
fun RatingRow(score: Int, setScore: (Int) -> Unit, text: String) {

    when(LocalConfiguration.current.orientation){

        Configuration.ORIENTATION_PORTRAIT->{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(Modifier.weight(1f)) {
                    Text(
                        text = text,
                        modifier = Modifier.padding(horizontal = 10.dp),
                        fontSize = 20.sp
                    )
                }
                Column(Modifier.weight(2f)) {

                    StarButtonIcon(score, setScore)
                }
            }
        }
        else -> {
            Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical=5.dp)) {
                Text(
                    text = text,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    fontSize = 20.sp
                )
            }
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical=5.dp)) {

                StarButtonIcon(score, setScore)
            }
        }
    }

}


@Composable
fun StarButtonIcon(score: Int, setScore: (Int) -> Unit) {

    LazyRow {
        items(5) { index ->
            IconButton(onClick = { setScore(index + 1) }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "starIcon",
                    tint = if (index < score) Color(0xFFFFB600)
                    else Color.Gray
                )
            }


        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        AddRatingScreen()
//    }
//}