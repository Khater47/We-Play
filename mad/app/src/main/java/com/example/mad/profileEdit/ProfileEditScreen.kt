package com.example.mad.profileEdit

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad.R
import com.example.mad.ui.theme.MadTheme
import com.example.mad.utils.getIconUserInfo
import com.example.mad.utils.getKeyboard

@Composable
fun ProfileEditScreen() {

    val configuration = LocalConfiguration.current

    Scaffold(
        topBar = { TopAppBarEditProfile() }
    ) {
        Box(Modifier.padding(it)) {

            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitEditProfile()
                }

                else -> {
                    LandscapeEditProfile()
                }
            }
        }
    }
}

@Composable
fun PortraitEditProfile() {

    Column(
        Modifier
            .fillMaxWidth(),
    ) {
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) { EditImageProfile() }

        Spacer(Modifier.padding(top = 40.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .padding(16.dp)
        ) { EditUserInfo() }


    }

}

@Composable
fun TopAppBarEditProfile() {

    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(
                text = "Edit Profile",
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(context, "Go Back", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.ArrowBack, "arrowBack", Modifier.size(28.dp))
            }
        },
        actions = {
            IconButton(onClick = {
                Toast.makeText(context, "Load Edit Profile", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.Edit, "Edit", Modifier.size(28.dp))
            }
        }

    )
}


//Image,FullName
@Composable
fun EditImageProfile() {

    val context = LocalContext.current
    var showMenu by remember {
        mutableStateOf(false)
    }

    Spacer(modifier = Modifier.padding(vertical = 5.dp))

    Box(
        Modifier.height(150.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )
        Box(
            Modifier
                .width(60.dp)
                .padding(start = 20.dp)
                .height(40.dp)
                .clip(CircleShape)
                .background(Color(14, 36, 50))
                .align(Alignment.TopEnd),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    showMenu=!showMenu
                }) {
                    Icon(
                        Icons.Default.PhotoCamera, "cameraIconButton",
                        Modifier.size(25.dp),
                        tint = Color.White
                    )
                }

                DropdownMenu(expanded = showMenu,
                    onDismissRequest = { showMenu=false }) {

                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text("Select image from gallery")
                    }

                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text("Take a picture")
                    }
                }

            }
        }
    }

}

@Composable
fun EditUserInfo() {

    val userInfo = listOf(
        "FullName",
        "Email",
        "Nickname",
        "PhoneNumber",
        "Description"
    )

    LazyColumn {
        items(userInfo, itemContent = { item ->
            EditInfo(item, getIconUserInfo(item))
        })
    }


}

@Composable
fun EditInfo(text: String, icon: ImageVector) {

    val info = remember { mutableStateOf("") }


    Row(
        Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            Modifier
                .padding(end = 10.dp)
                .size(28.dp)
        ) {
            Icon(
                icon,
                "iconTextField",
            )
        }

        OutlinedTextField(
            value = info.value,
            onValueChange = {
                info.value = it
            },
            label = { Text(text = text) },
            keyboardOptions = KeyboardOptions(
                keyboardType = getKeyboard(text),
                imeAction = ImeAction.Done
            ),

            )
    }
}


@Composable
fun LandscapeEditProfile() {

    Row(
        Modifier
            .fillMaxWidth()
    ) {

        Column(
            Modifier
                .weight(1f)
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditImageProfile()
        }


        Column(
            Modifier
                .weight(2f)
                .padding(start = 20.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            EditUserInfo()
        }


    }
}

//Portrait Preview

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ProfileEditScreen()
    }
}

//Landscape Preview

//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        ProfileEditScreen()
//    }
//}