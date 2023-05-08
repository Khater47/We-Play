package com.example.mad.profile

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.mad.ui.theme.MadTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.SportsGolf
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mad.R
import com.example.mad.utils.getIconSport

@Composable
fun ProfileScreen() {

    val configuration = LocalConfiguration.current

    Scaffold(
        topBar = { TopAppBarProfile() }
    ) {
        Box(Modifier.padding(it)) {

            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitProfile()
                }

                else -> {
                    LandscapeProfile()
                }
            }
        }
    }
}

@Composable
fun TopAppBarProfile() {

    val context = LocalContext.current

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
                Toast.makeText(context, "Load Edit Profile", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.Edit, "Edit", Modifier.size(28.dp))
            }
        }

    )
}

@Composable
fun PortraitProfile() {

    val sports = listOf<String>(
        "Soccer",
        "Baseball",
        "Basketball",
        "Cricket",
        "Football",
        "Golf",
        "Gymnastic",
        "Tennis",
        "Volleyball"
    )

    Column(
        Modifier
            .fillMaxWidth(),

        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) { ImageProfile() }

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
            ) { UserInfo() }

            LazyRow{
                items(sports, itemContent = {
                    item -> ChipSport(text = item, icon = getIconSport(item))
                })
            }

            Spacer(Modifier.padding(50.dp))
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipSport(text:String,icon:ImageVector){

    ElevatedSuggestionChip(onClick = {},
        label={Text(text)},
        icon={Icon(icon,contentDescription = "iconSport")}
    )

}

@Composable
fun LandscapeProfile() {

    Row {
        Column(
            Modifier
                .weight(2f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) { ImageProfile() }
        Column(
            Modifier
                .weight(3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) { UserInfo() }

    }

}

//Image,FullName,Email
@Composable
fun ImageProfile() {

    Spacer(modifier = Modifier.padding(vertical = 5.dp))
    Image(
        painter = painterResource(id = R.drawable.profile),
        contentDescription = "Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)

    )
    Text(text = "Mario Rossi", fontSize = 20.sp, modifier = Modifier.padding(vertical = 10.dp))
}

//Nickname,PhoneNumber,Description
@Composable
fun UserInfo() {

    val body = MaterialTheme.typography.body1
    val modifierText = Modifier.padding(vertical = 15.dp)

    Text(text = "Personal Info", modifier = modifierText, style = body, fontSize = 20.sp)
    Divider()

    LabelText("mariorossi@gmail.com", Icons.Outlined.Email)
    Divider()

    LabelText("mario", Icons.Outlined.AlternateEmail)
    Divider()

    LabelText("1234567890", Icons.Default.Phone)
    Divider()

    LabelText("student", Icons.Default.Description)

}

@Composable
fun LabelText(text: String, icon: ImageVector) {
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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        ProfileScreen()
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
