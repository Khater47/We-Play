package com.example.mad.screens.profile

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mad.R
import com.example.mad.common.composable.CircleImage
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextBasicIcon
import com.example.mad.common.composable.TopBarAction
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.common.composable.TopBarComplete
import com.example.mad.common.getImageFromInternalStorage
import com.example.mad.ui.theme.MadTheme

@Composable
fun ProfileScreen(
//    navController: NavHostController,
) {

    val orientation = LocalConfiguration.current.orientation
    val image = getImageFromInternalStorage(LocalContext.current)
    val fullName = "Mario Rossi"

    fun getEditProfile() {
        val route = "/editProfile"
//        navController.navigate(route)
    }

    Scaffold(
        topBar = { TopBarAction(R.string.topBarUserProfile,Icons.Default.Check ,::getEditProfile) }
    ) {
        Box(Modifier.padding(it)) {

            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitProfile(image,
                        fullName,

                    )
                }

                else -> {
                    LandscapeProfile(image,
                        fullName,

                        )
                }
            }
        }
    }

}

@Composable
fun PortraitProfile(
    image: Bitmap,
    fullName: String,
//    userObject: Map<String, String>
    ) {


    Column {

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImageContainer(image, fullName)
        }

        Column(
            Modifier
                .weight(3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            UserInfo(/*userObject*/)

        }

    }
}

@Composable
fun ImageContainer(image: Bitmap, fullName: String) {

    Spacer(modifier = Modifier.padding(vertical = 5.dp))

    CircleImage(image.asImageBitmap())

    Spacer(modifier = Modifier.padding(vertical = 5.dp))

    TextBasicHeadLine(fullName)
}

@Composable
fun UserInfo(
//    userObject:Map<String,String>
) {

    val modifier=Modifier.padding(vertical=20.dp)

    Card(elevation=CardDefaults.cardElevation(),
    shape= RoundedCornerShape(10.dp),
        modifier= Modifier
            .padding(10.dp)
    ){

        Spacer(modifier=Modifier.padding(top=20.dp))

        TextBasicIcon(text = "Email", icon = Icons.Outlined.Email)
        Divider(modifier=modifier)

        TextBasicIcon(text = "Nickname", icon = Icons.Outlined.AlternateEmail)
        Divider(modifier=modifier)

        TextBasicIcon(text = "PhoneNumber", icon = Icons.Outlined.Phone)
        Divider(modifier=modifier)

        TextBasicIcon(text = "Description", icon = Icons.Outlined.Description)
        Spacer(modifier=Modifier.padding(bottom=20.dp))
    }



}

@Composable
fun LandscapeProfile(
    image: Bitmap,
    fullName: String,
//    userObject:Map<String,String>
) {

    Row {
        Column(
            Modifier
                .weight(2f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            ImageContainer(image, fullName)
        }

        Column(
            Modifier
                .weight(3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            UserInfo(/*userObject*/)

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreviewProfile() {
    MadTheme {
        ProfileScreen()
    }
}


@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
@Composable
fun DefaultPreviewProfileLandscape() {
    MadTheme {
        ProfileScreen()
    }
}
