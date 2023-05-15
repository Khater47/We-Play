package com.example.mad.profileRating

import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
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
                PlaygroundRating(vm)
        }
    }


}

@Composable
fun StarButtonIcon(score:Int,setScore:(Int)->Unit){

    LazyRow {
        items(5) { index ->
                IconButton(onClick = { setScore(index+1) }) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "starIcon",
                        tint =
                        if(index<score) Color(0xFFFFB600)
                        else Color.Gray
                    )
                }


        }
    }
}

@Composable
fun DialogSetRating(vm:UserViewModel,id:Int,playground:String,idPlayground:Int,userId:String,setShowDialog:(Boolean)->Unit){

    val (quality, setQuality) = remember { mutableStateOf(0) }
    val (facilities, setFacilities) = remember { mutableStateOf(0) }


    Dialog(onDismissRequest = { setShowDialog(false) }) {
        androidx.compose.material3.Card(shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp,Color.LightGray),
            colors = CardDefaults.cardColors()
        ) {
            Column(Modifier.fillMaxWidth()) {

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "Rating for $playground",
                        fontSize = 22.sp,
                    )
                }


                Column(Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    Text(text="Quality: ", fontSize = 20.sp)
                }

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    StarButtonIcon(score = quality, setScore = setQuality)

                }

                Column(Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text="Facilities: ", fontSize = 20.sp)
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    StarButtonIcon(score = facilities, setScore = setFacilities)

                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(onClick = {
                        setShowDialog(false)
                    },
                        colors= ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                        shape= CircleShape,
                        modifier= Modifier
                            .weight(1f)
                            .padding(end = 5.dp),
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {


                        val p = ProfileRating(id,quality,facilities,userId.toInt(),idPlayground)

                        vm.insertProfileRating(p)

                        setShowDialog(false)

                    },
                        shape= CircleShape,
                        modifier= Modifier
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
fun PlaygroundRating(vm:UserViewModel){

    val playgrounds:List<Playgrounds> by
        vm.playgrounds.observeAsState(emptyList())

    val ratingPlaygrounds by vm.getProfileRatingByIdProfile(2).observeAsState(emptyList())
    

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(ratingPlaygrounds,itemContent = { item ->
            Card(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
            ) {
                val playground = playgrounds.firstOrNull(){it.id==item.idPlayground}

                val playgroundTitle = playground?.playground?:""
                val playgroundSport = playground?.sport?:""
                val playgroundLocation = playground?.location?:""


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

                    Column(modifier=Modifier.fillMaxWidth().padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                        Button(
                            onClick={
                            val first = ratingPlaygrounds.filter{it.idPlayground==item.id}
                            if(first.size==1){
                                val p = ProfileRating(first[0].id,
                                first[0].quality,
                                first[0].facilities,
                                first[0].idPlayground,
                                first[0].idProfile)
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
        })
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