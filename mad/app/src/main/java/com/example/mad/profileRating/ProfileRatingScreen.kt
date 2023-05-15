package com.example.mad.profileRating

import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import com.example.mad.activity.BottomBarScreen
import com.example.mad.model.Playgrounds
import com.example.mad.model.ProfileRating
import com.example.mad.utils.getIconPlayground
import com.example.mad.utils.getIconSport


@Composable
fun ProfileRatingScreen(navController:NavHostController,vm:UserViewModel) {

    Scaffold(
        topBar = { TopAppBarRating(navController) }
    ){
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)) {
                PlaygroundRating(navController,vm)
        }
    }


}




@Composable
fun PlaygroundRating(navController: NavHostController,vm:UserViewModel){

    val playgrounds:List<Playgrounds> by
        vm.playgrounds.observeAsState(emptyList())

    val ratingPlaygrounds by vm.getProfileRatingByIdProfile(2).observeAsState(emptyList())
    
    Column(Modifier.fillMaxSize()) {

        LazyColumn(modifier = Modifier
            .padding(16.dp)
            .weight(4f)) {
            items(ratingPlaygrounds,itemContent = { item ->
                Card(
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    val playground = playgrounds.firstOrNull{it.id==item.idPlayground}

                    if(playground!=null){
                        val idPlayground = playground.id
                        val playgroundTitle = playground.playground
                        val playgroundSport = playground.sport
                        val playgroundLocation = playground.location

                        Column(){
                            Text(text = playgroundTitle,modifier=Modifier.padding(10.dp))

                            Image(
                                painter = painterResource(id = getIconPlayground(playgroundSport)),
                                contentDescription = "Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                            )

                            Row(modifier=Modifier.fillMaxWidth()){

                                Column(Modifier.weight(1f)){
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Icon(imageVector = Icons.Default.LocationOn,
                                            contentDescription = "IconLocation",
                                            modifier=Modifier.padding(5.dp))
                                        Text(text = playgroundLocation,
                                            modifier=Modifier.padding(10.dp))
                                    }

                                }
                                Column(Modifier.weight(1f)){
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Icon(imageVector = getIconSport(playgroundSport),
                                            contentDescription = "IconLocation",
                                            modifier=Modifier.padding(5.dp))
                                        Text(text = playgroundSport,
                                            modifier=Modifier.padding(10.dp))
                                    }
                                }
                            }

                            Row(modifier=Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically){

                                Column(Modifier.weight(1f)) {
                                    Text(text="Quality: ",modifier=Modifier.padding(10.dp))
                                }
                                Column(Modifier.weight(3f)) {


                                    StarIcon(score = item.quality)
                                }
                            }

                            Row(modifier=Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically){

                                Column(Modifier.weight(1f)) {
                                    Text(text="Facilities: ",modifier=Modifier.padding(10.dp))
                                }
                                Column(Modifier.weight(3f)) {

                                    StarIcon(score = item.facilities)
                                }
                            }

                            Column(modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center) {
                                Button(
                                    onClick={
                                        val first = ratingPlaygrounds.firstOrNull{it.idPlayground==idPlayground}
                                        if(first!=null){
                                            val p = ProfileRating(first.id,
                                                first.quality,
                                                first.facilities,
                                                first.idPlayground,
                                                first.idProfile)

                                            vm.deleteProfileRating(p)
                                        }

                                    }){
                                    Row(horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically){
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Rating Playground",modifier=Modifier.padding(horizontal = 10.dp))
                                        Text(text="Delete")
                                    }
                                }
                            }

                        }
                    }



                }
            })
        }

        Column(Modifier.weight(1f).fillMaxWidth().padding(horizontal=10.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center) {
            Button(onClick = {
                          navController.navigate(BottomBarScreen.AddRating.route)
            }, Modifier.clip(CircleShape).size(60.dp)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "addIcon")
            }
        }
                
    }



}

@Composable
fun StarIcon(score:Int?){

    var newScore by remember{
        mutableStateOf(score?:0)
    }


    LazyRow(){
        items(5) { index ->
            if(index<newScore){
                Icon(imageVector = Icons.Default.Star, contentDescription = "starIcon",
                    tint= Color(0xFFFFB600))
            }

            else {
                Icon(imageVector = Icons.Default.Star, contentDescription = "starIcon",
                    tint= Color.Gray)
            }
        }
    }

}

@Composable
fun TopAppBarRating(navController:NavHostController) {


    TopAppBar(
        title = {
            Text(
                text = "User Profile Rating",
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