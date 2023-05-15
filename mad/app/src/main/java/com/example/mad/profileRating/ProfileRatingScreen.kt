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

//    var ratingPlaygrounds  by remember { mutableStateOf<List<ProfileRating>>(emptyList()) }

//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    vm.getProfileRatingByIdProfile(2).observe(lifecycleOwner) {
//        ratingPlaygrounds=it.toList()
//    }

    var id by remember {
        mutableStateOf(0)
    }

    var playground by remember {
        mutableStateOf("")
    }
    var idPlayground by remember {
        mutableStateOf(-1)
    }

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }


    if(showDialog){
        DialogSetRating(vm,id,playground, idPlayground = idPlayground, userId = "2", setShowDialog = setShowDialog)
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(playgrounds,itemContent = { item ->
            Card(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickable {
                        setShowDialog(true)
                        playground = item.playground
                        idPlayground = item.id
                        val first = ratingPlaygrounds.filter { it.idPlayground == item.id }
                        Log.d("TAG", first.size.toString())
                        id = if (first.isNotEmpty()) {
                            first[0].id
                        } else {
                            0
                        }
                    },
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(){
                    Text(text = item.playground,modifier=Modifier.padding(10.dp))

                    Image(
                        painter = painterResource(id = getIconPlayground(item.sport)),
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
                                Text(text = item.location,
                                    modifier=Modifier.padding(10.dp))
                            }

                        }
                        Column(Modifier.weight(1f)){
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Icon(imageVector = getIconSport(item.sport),
                                    contentDescription = "IconLocation",
                                    modifier=Modifier.padding(5.dp))
                                Text(text = item.sport,
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
                            val first = ratingPlaygrounds.filter { it.idPlayground == item.id }

                            val q = if(first.isNotEmpty()){
                                first[0].quality?:0

                            } else {
                                0
                            }

                            StarIcon(score = q,item.id,item.playground)
                        }
                    }

                    Row(modifier=Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically){

                        Column(Modifier.weight(1f)) {
                            Text(text="Facilities: ",modifier=Modifier.padding(10.dp))
                        }
                        Column(Modifier.weight(3f)) {
                            val first = ratingPlaygrounds.filter { it.idPlayground == item.id }

                            val f = if(first.isNotEmpty()){
                                first[0].facilities?:0
                            } else {
                                0
                            }


                            StarIcon(score = f,item.id,item.playground)
                        }
                    }

                    Column(modifier=Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                        Button(onClick={
                            val first = ratingPlaygrounds.filter{idPlayground==item.id}
                            if(first.size==1){
                                val p = ProfileRating(first[0].id,
                                first[0].quality,
                                first[0].facilities,
                                first[0].idPlayground,
                                first[0].idProfile)
                                vm.deleteProfileRating(p)
                            }

                        }){
                            Text(text="Reset")
                        }
                    }

                }


            }
        })
    }



}

@Composable
fun StarIcon(score:Int?,idPlayground: Int,playground:String){

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