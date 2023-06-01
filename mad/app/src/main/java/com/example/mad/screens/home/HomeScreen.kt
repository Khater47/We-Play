package com.example.mad.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.ImageCardHome
import com.example.mad.common.composable.TopBarAction
import com.example.mad.common.composable.TopBarBasic

@Composable
fun HomeScreen(
navController: NavHostController,
) {
    val configuration = LocalConfiguration.current

    fun navigate(){
        navController.navigate(BottomBarScreen.Notifications.route)
    }

    Scaffold(
        topBar = { TopBarAction(R.string.topBarHome,Icons.Default.Notifications,::navigate) }
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
fun PortraitHome(
    navController: NavHostController,
){

    Column {
        Column(
            Modifier
                .padding(horizontal = 20.dp)
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
fun LandscapeHome(
    navController: NavHostController,

){

    Column(
        Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){

        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 40.dp, end = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {

            CardUserPreferences(navController)
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 30.dp),
        ){
            CardContainerNavigationPage(navController)
        }
    }
}


@Composable
fun CardContainerNavigationPage(
    navController: NavHostController,
) {

    val searchIcon = Icons.Default.Search
    val rateIcon = Icons.Default.ThumbUp
    val idSearch = R.string.rentCard
    val idRating = R.string.rateCard

    Row {
        Column(
            Modifier
                .weight(1f)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardNavigationPage(searchIcon, R.drawable.field, idSearch,
                navController)
        }

        Column(
            Modifier
                .weight(1f)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardNavigationPage(rateIcon, R.drawable.field_rating, idRating,
                navController)
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardUserPreferences(
    navController: NavHostController
) {

    val orientation = LocalConfiguration.current.orientation

    val modifier = if(orientation==Configuration.ORIENTATION_PORTRAIT){
        Modifier
            .fillMaxWidth()
            .height(100.dp)
    }
    else {
        Modifier
            .fillMaxWidth()
            .height(70.dp)
    }

    Card(onClick={
                 navController.navigate(BottomBarScreen.ProfileSport.route)
    },
        modifier=modifier,
        shape = RoundedCornerShape(16.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors=CardDefaults.cardColors(Color.White)
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Icon(Icons.Default.SportsCricket, "Sport Image")

            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(5f)
            ) {
                Text(text = stringResource(R.string.sportCard),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp)

            }


        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardNavigationPage(
    searchIcon: ImageVector,
    imageCard: Int,
    id: Int,
    navController: NavHostController,
) {

    val route =
        if (imageCard == R.drawable.field) BottomBarScreen.SearchField.route
        else BottomBarScreen.ProfileRating.route


    Card(
        onClick = {
            navController.navigate(route)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors=CardDefaults.cardColors(Color.White)
        ) {

        Column {
            ImageCardHome(searchIcon, imageCard)
//
            TextCard(id)

        }


    }
}



@Composable
fun TextCard(id:Int) {

    val text = stringResource(id)

    Column(
        Modifier
            .padding(10.dp)
            .fillMaxSize(),
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)
    }

}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        HomeScreen()
//    }
//}
////
////
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewLandscape() {
//
//    MadTheme {
//        HomeScreen()
//    }
//}