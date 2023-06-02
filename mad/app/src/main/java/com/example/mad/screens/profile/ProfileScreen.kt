package com.example.mad.screens.profile

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CircleImage
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TopBarProfile
import com.example.mad.common.getImageFromInternalStorage
import com.example.mad.model.Profile

/*
TODO:
    1) top bar title and icon in white
*/

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(
    navController: NavHostController,
    vm: MainViewModel,
) {
    val loading = vm.loadingProgressBar.value

    val orientation = LocalConfiguration.current.orientation
    val image = getImageFromInternalStorage(LocalContext.current)

    val user = vm.profile.observeAsState().value?: Profile(
        "",
        "",
        "",
        "",
        ""
    )

    LaunchedEffect(key1 = null){
        if(vm.currentUser.value!=null)
            vm.getUserProfile()
    }

    if(vm.currentUser.value?.uid==null){
        navController.navigate(BottomBarScreen.Login.route)
    }

    Scaffold(
        topBar = { TopBarProfile(vm, navController) }
    ) {
        Box(Modifier.padding(it)) {

            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitProfile(
                        image,
                        user
                    )
                }

                else -> {
                    LandscapeProfile(
                        image,
                        user
                    )
                }
            }

            CircularProgressBar(isDisplayed = loading)
        }
    }

}

@Composable
fun PortraitProfile(
    image: Bitmap,
    user: Profile
) {

    Column {

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImageContainer(image, user.fullName)
        }

        Column(
            Modifier
                .weight(3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            UserInfo(user)

        }

    }
}

@Composable
fun ImageContainer(
    image: Bitmap,
    fullName: String
) {

    val orientation = LocalConfiguration.current.orientation

    val modifier =
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            Modifier.padding(vertical = 10.dp)
        else Modifier.padding(vertical = 2.dp)

    Spacer(modifier = modifier)

    CircleImage(image.asImageBitmap())

    Spacer(modifier = Modifier.padding(vertical = 5.dp))

    TextBasicHeadLine(fullName)
}

@Composable
fun UserInfo(
    user: Profile
) {

    val modifier = Modifier.padding(vertical = 20.dp)

    Card(
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors(
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(10.dp)
    ) {

        Column(modifier = Modifier.padding(10.dp)) {
            TextBasicHeadLine(text = "Personal Info")
        }
        Divider(Modifier.padding(top = 10.dp))

        Spacer(modifier = Modifier.padding(top = 20.dp))

        TextIcon(text = user.email, icon = Icons.Outlined.Email)

        Divider(modifier = modifier)

        TextIcon(text = user.nickname, icon = Icons.Outlined.AlternateEmail)
        Divider(modifier = modifier)

        TextIcon(text = user.phone, icon = Icons.Outlined.Phone)
        Divider(modifier = modifier)

        TextIcon(text = user.description, icon = Icons.Outlined.Description)

        Spacer(modifier = Modifier.padding(bottom = 20.dp))
    }


}

@Composable
fun TextIcon(text: String, icon: ImageVector) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = "iconLabel",
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text, style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp
        )
    }
}

@Composable
fun LandscapeProfile(
    image: Bitmap,
    user: Profile
) {

    Row {
        Column(
            Modifier
                .weight(2f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            ImageContainer(image, user.fullName)
        }

        Column(
            Modifier
                .weight(3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(2.dp)
                .verticalScroll(rememberScrollState())
        ) {
            UserInfo(user)

        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewProfile() {
//    MadTheme {
//        ProfileScreen()
//    }
//}
//
//
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewProfileLandscape() {
//    MadTheme {
//        ProfileScreen()
//    }
//}
