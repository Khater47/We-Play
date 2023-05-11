package com.example.mad.profile

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import com.example.mad.UserViewModel
import com.example.mad.utils.getImageFromInternalStorage

@Composable
fun ProfileScreen(navController:NavHostController,vm:UserViewModel,userId:String?) {

    val configuration = LocalConfiguration.current

    val lifecycleOwner = LocalLifecycleOwner.current

    val userObject = remember{ mutableStateMapOf(
        "FullName" to "Mario Verdi",
        "Email" to "marioverdi@gmail.com",
        "Nickname" to "mario_verdi",
        "PhoneNumber" to "3123467890",
        "Description" to "worker"
    ) }

    vm.getProfileById(userId?.toInt()?:2).observe(lifecycleOwner, Observer {
            user ->
                if(user!=null){
                    userObject["userId"] = user.id.toString()
                    userObject["FullName"] = user.fullName
                    userObject["Email"] = user.email
                    userObject["Nickname"] = user.nickname
                    userObject["PhoneNumber"] = user.phone
                    userObject["Description"] = user.description
                }
    })

    Scaffold(
        topBar = { TopAppBarProfile(navController,userId?:"2") }
    ) {
        Box(Modifier.padding(it)) {

            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitProfile(userObject)
                }

                else -> {
                    LandscapeProfile(userObject)
                }
            }
        }
    }
}

@Composable
fun TopAppBarProfile(navController:NavHostController,userId:String) {

    TopAppBar(
        title = {
                Text(
                    text = "User Profile",
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
        },
        navigationIcon = {},
        actions = {
            IconButton(onClick = {
                navController.navigate("profileEdit/$userId")
            }) {
                Icon(Icons.Default.Edit, "Edit", Modifier.size(28.dp))
            }
        }

    )
}

@Composable
fun PortraitProfile(userObject: Map<String, String>) {

    Column(
        Modifier
            .fillMaxWidth(),

        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) { ImageProfile(userObject.getValue("FullName")) }

        Column(
            Modifier
                .weight(2f)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) { UserInfo(userObject) }
        }

    }


}

@Composable
fun LandscapeProfile(userObject: Map<String, String>) {

    Row {
        Column(
            Modifier
                .weight(2f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) { ImageProfile(userObject.getValue("FullName")) }
        Column(
            Modifier
                .weight(3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) { UserInfo(userObject) }

    }

}

//Image,FullName
@Composable
fun ImageProfile(fullName:String) {

    val context = LocalContext.current
    val image = getImageFromInternalStorage(context)


    Spacer(modifier = Modifier.padding(vertical = 5.dp))

    Image(
        bitmap=image.asImageBitmap() ,
        contentDescription = "Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)

    )
    Text(text = fullName, fontSize = 20.sp, modifier = Modifier.padding(vertical = 10.dp))
}

//Email,Nickname,PhoneNumber,Description
@Composable
fun UserInfo(userObject:Map<String,String>) {

    val body = MaterialTheme.typography.body1
    val modifierText = Modifier.padding(vertical = 15.dp)

    Text(text = "Personal Info", modifier = modifierText, style = body, fontSize = 20.sp)

    ProfileInfo(userObject.getValue("Email"), Icons.Outlined.Email)
    Divider()

    ProfileInfo(userObject.getValue("Nickname"), Icons.Outlined.AlternateEmail)
    Divider()

    ProfileInfo(userObject.getValue("PhoneNumber"), Icons.Default.Phone)
    Divider()

    ProfileInfo(userObject.getValue("Description"), Icons.Default.Description)

}

@Composable
fun ProfileInfo(text: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column {
            Icon(icon, text)
        }
        Column(modifier = Modifier.padding(horizontal = 5.dp)) {
            Text(
                text,
                modifier = Modifier.padding(vertical = 15.dp),
                style = MaterialTheme.typography.body1,
                fontSize = 18.sp
            )

        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        ProfileScreen()
//    }
//}

//Landscape Preview
//
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        HomeScreen()
//    }
//}
