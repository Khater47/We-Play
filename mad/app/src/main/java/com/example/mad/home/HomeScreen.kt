package com.example.mad.home


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen


@Composable
fun HomeScreen(navController: NavHostController) {

    val configuration = LocalConfiguration.current

    Scaffold(
        topBar = { TopAppBarHome() }
    ) {
        Box(Modifier.padding(it)) {

            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitHome(navController)
                }

                else -> {
                    LandscapeHome(navController)
                }
            }
        }
    }
}


@Composable
fun PortraitHome(navController: NavHostController) {

    Column(
        Modifier
            .fillMaxWidth(),

        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardUserPreferences(navController)
        }

        Column(
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .weight(2f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {

            CardContainerNavigationPage(navController)

        }
    }


}

@Composable
fun LandscapeHome(navController: NavHostController) {

    val searchIcon = Icons.Default.Search
    val searchImage = R.drawable.field
    val rateIcon = Icons.Default.ThumbUp
    val ratingImage = R.drawable.field_rating
    val textSearch = "Book field"
    val textRating = "Rate field"


    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 30.dp, end = 30.dp),
            verticalArrangement = Arrangement.Center
        ) {
            CardUserPreferences(navController)
        }

        Row(
            Modifier.padding(top = 10.dp, start = 30.dp, end = 30.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                CardNavigationPage(searchIcon, searchImage, textSearch, navController)
            }

            Column(
                Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                CardNavigationPage(rateIcon, ratingImage, textRating, navController)
            }
        }


    }
}


@Composable
fun CardContainerNavigationPage(navController: NavHostController) {

    val searchIcon = Icons.Default.Search
    val searchImage = R.drawable.field
    val rateIcon = Icons.Default.ThumbUp
    val ratingImage = R.drawable.field_rating
    val textSearch = "Book field"
    val textRating = "Rate field"

    Row {
        Column(
            Modifier
                .weight(1f)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardNavigationPage(searchIcon, searchImage, textSearch, navController)
        }

        Column(
            Modifier
                .weight(1f)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardNavigationPage(rateIcon, ratingImage, textRating, navController)
        }

    }
}

@Composable
fun CardNavigationPage(
    searchIcon: ImageVector,
    imageCard: Int,
    textCard: String,
    navController: NavHostController
) {

    val route =
        if (imageCard == R.drawable.field) BottomBarScreen.RentField.route else BottomBarScreen.ProfileRating.route


    val interactionSource = remember { MutableInteractionSource() }
    val rippleIndication = rememberRipple(bounded = true)


    Card(
        Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = rippleIndication,
                onClick = {
                    navController.navigate(route)
                }
            ),
        elevation = 10.dp,
        shape = RoundedCornerShape(16.dp),

        ) {

        Column {
            ImageCard(searchIcon, imageCard)

            TextCard(textCard)

        }


    }
}


@Composable
fun TopAppBarHome() {


    TopAppBar(
        title = {
            Text(
                text = "Court Reservation Application",
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {},
        actions = {}

    )
}


@Composable
fun CardUserPreferences(navController: NavHostController) {


    Card(
        Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 10.dp

    ) {
        Row(
            Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(Icons.Default.Sports, "Sport Image")

            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(3f)
            ) {
                Text(
                    "Edit your sport preferences",
                    modifier = Modifier.padding(vertical = 15.dp),
                    style = MaterialTheme.typography.body1,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis

                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    navController.navigate(BottomBarScreen.ProfileSport.route)
                }) {
                    Icon(Icons.Default.ArrowForward, "Edit", Modifier.size(28.dp))
                }

            }

        }
    }


}


@Composable
fun ImageCard(icon: ImageVector, image: Int) {

    Box(
        Modifier.height(104.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(85.dp)
                .fillMaxWidth()
        )
        Box(
            Modifier
                .width(60.dp)
                .padding(start = 20.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(14, 36, 50))
                .align(Alignment.BottomStart),
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    icon, "Image Icon Card",
                    Modifier.size(25.dp),
                    tint = Color.White
                )

            }
        }
    }
}


@Composable
fun TextCard(headerText: String) {

    Column(
        Modifier
            .padding(10.dp)
            .fillMaxSize(),
    ) {
        Text(
            text = headerText,
            fontSize = 16.sp,
            maxLines = 1,
        )
    }

}

//Landscape Preview
//
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        HomeScreen()
//    }
//}

//Portrait Preview

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        HomeScreen()
//    }
//}
