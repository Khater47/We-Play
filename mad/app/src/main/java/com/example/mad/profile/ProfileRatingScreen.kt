package com.example.mad.profile

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import com.example.mad.activity.BottomBarScreen
import com.example.mad.model.Playgrounds
import com.example.mad.model.ProfileRating
import com.example.mad.utils.getIconPlayground
import com.example.mad.utils.getIconSport


@Composable
fun ProfileRatingScreen(navController: NavHostController, vm: UserViewModel) {

    Scaffold(
        topBar = { TopAppBarRating(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(BottomBarScreen.AddRating.route)
                },
                containerColor = Color(0xFF6750A4),
                modifier=Modifier.clip(CircleShape)
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
            PlaygroundRating(vm)
        }
    }


}


@Composable
fun PlaygroundRating(vm: UserViewModel) {

    val playgrounds: List<Playgrounds> by
    vm.playgrounds.observeAsState(emptyList())

    val ratingPlaygrounds by vm.getProfileRatingByIdProfile(2).observeAsState(emptyList())

    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column(Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(4f)
                ) {
                    items(ratingPlaygrounds, itemContent = { item ->
                        CardPlaygroundPortrait(
                            playgrounds,
                            vm,
                            item,
                            ratingPlaygrounds
                        )
                    })
                }
            }

        }

        else -> {
            Row(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                ) {
                    items(ratingPlaygrounds, itemContent = { item ->
                        Row{
                            Column(Modifier.weight(1f)) {
                                CardPlaygroundLandscape(
                                    playgrounds,
                                    item,
                                )

                            }
                            Column(Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                val playground = playgrounds.firstOrNull { it.id == item.idPlayground }
                                val idPlayground = playground?.id ?: -1


                                Column(Modifier.padding(10.dp)) {
                                    RatingRow(item.quality,"Quality")
                                }

                                Column(Modifier.padding(10.dp)) {
                                    RatingRow(item.facilities,"Facilities")
                                }

                                Column(modifier=Modifier.padding(vertical=10.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally){
                                    DeleteRatingPlaygroundButton(
                                        vm,
                                        ratingPlaygrounds,
                                        idPlayground
                                    )
                                }
                            }
                        }

                    })
                }

            }
        }
    }


}

@Composable
fun CardPlaygroundLandscape(
    playgrounds: List<Playgrounds>,
    item: ProfileRating,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = 16.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        val playground = playgrounds.firstOrNull { it.id == item.idPlayground }

        if (playground != null) {
            val playgroundTitle = playground.playground
            val playgroundSport = playground.sport
            val playgroundLocation = playground.location

            Column {
                Text(
                    text = playgroundTitle,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 20.sp
                )

                Image(
                    painter = painterResource(id = getIconPlayground(playgroundSport)),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                )

                InfoPlayground(playgroundLocation, playgroundSport)
            }

        }
    }
}

@Composable
fun CardPlaygroundPortrait(
    playgrounds: List<Playgrounds>,
    vm: UserViewModel,
    item: ProfileRating,
    ratingPlaygrounds: List<ProfileRating>
) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        elevation = 16.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        val playground = playgrounds.firstOrNull { it.id == item.idPlayground }

        if (playground != null) {
            val idPlayground = playground.id
            val playgroundTitle = playground.playground
            val playgroundSport = playground.sport
            val playgroundLocation = playground.location

            Column {
                Text(
                    text = playgroundTitle,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 20.sp
                )

                Image(
                    painter = painterResource(id = getIconPlayground(playgroundSport)),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                )

                InfoPlayground(playgroundLocation, playgroundSport)

                RatingRow(item.quality,"Quality")

                RatingRow(item.facilities,"Facilities")

                DeleteRatingPlaygroundButton(
                    vm,
                    ratingPlaygrounds,
                    idPlayground
                )

            }
        }


    }


}

@Composable
fun DeleteRatingPlaygroundButton(
    vm: UserViewModel,
    ratingPlaygrounds: List<ProfileRating>,
    idPlayground: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val first = ratingPlaygrounds.firstOrNull { it.idPlayground == idPlayground }
                if (first != null) {
                    val p = ProfileRating(
                        first.id,
                        first.quality,
                        first.facilities,
                        first.idPlayground,
                        first.idProfile
                    )

                    vm.deleteProfileRating(p)
                }

            }) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Rating Playground",
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(text = "Delete")
            }
        }
    }
}

@Composable
fun InfoPlayground(playgroundLocation: String, playgroundSport: String) {
    Row(modifier = Modifier.fillMaxWidth()) {

        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "IconLocation",
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = playgroundLocation,
                    modifier = Modifier.padding(10.dp)
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
                    text = playgroundSport,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun RatingRow(score: Int?,text:String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
            Text(text = text, modifier = Modifier.padding(10.dp), fontSize = 18.sp)
        }
        Column(
            Modifier
                .weight(2f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            StarIcon(score = score)
        }
    }

}

@Composable
fun StarIcon(score: Int?) {

    val newScore by remember {
        mutableStateOf(score ?: 0)
    }


    LazyRow {
        items(5) { index ->
            if (index < newScore) {
                Icon(
                    imageVector = Icons.Default.Star, contentDescription = "starIcon",
                    tint = Color(0xFFFFB600)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Star, contentDescription = "starIcon",
                    tint = Color.Gray
                )
            }
        }
    }

}

@Composable
fun TopAppBarRating(navController: NavHostController) {


    TopAppBar(
        title = {
            Text(
                text = BottomBarScreen.ProfileRating.title,
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

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        ProfileRatingScreen()
//    }
//}


//        Column(
//            Modifier
//                .weight(1f)
//                .fillMaxWidth()
//                .padding(horizontal = 10.dp),
//            horizontalAlignment = Alignment.End,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Button(
//                onClick = {
//                    navController.navigate(BottomBarScreen.AddRating.route)
//                },
//                Modifier
//                    .clip(CircleShape)
//                    .size(60.dp)
//            ) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = "addIcon")
//            }
//        }