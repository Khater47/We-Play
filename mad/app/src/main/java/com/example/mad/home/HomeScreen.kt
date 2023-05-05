package com.example.mad.home


import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sports
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mad.R

@Composable
fun HomeScreen() {

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


    TopAppBar(
        title = {
            Text(
                text = "Home Page",
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
fun PortraitProfile() {

    Column(
        Modifier
            .fillMaxWidth(),

        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardUserPreferences()

        }

        Column(
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .padding(16.dp)
                .weight(2f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {

            CardContainerNavigationPage()

        }
    }


}

@Composable
fun LandscapeProfile() {

    Column(
        Modifier
            .fillMaxWidth(),
        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f).padding(top=5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardUserPreferences()
        }

        Column(
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .padding(16.dp)
                .weight(2f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {

            CardContainerNavigationPage()

        }
    }

}

@Composable
fun CardUserPreferences() {

    val context = LocalContext.current


    Card(
        Modifier
            .width(350.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray),
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
                    Toast.makeText(context, "Go to Edit Sport Profile", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Default.ArrowForward, "Edit", Modifier.size(28.dp))
                }

            }

        }
    }


}


@Composable
fun CardContainerNavigationPage() {

    Row() {
        Column(
            Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
            CardNavigationPage()
        }
        Column(
            Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
            CardNavigationPage()

        }
    }
}

@Composable
fun CardNavigationPage() {

    Card(
        Modifier
            .fillMaxSize()
            .border(BorderStroke(1.dp, Color.LightGray)),
        elevation = 10.dp,
        shape = RoundedCornerShape(16.dp),

        ) {

        Column() {
            ImageCard(Icons.Default.Search,R.drawable.field)

            TextCard("Prenota un campo")

        }


    }
}


@Composable
fun ImageCard(icon: ImageVector,image:Int) {

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

    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column(
                Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = headerText,
                    fontSize = 13.sp,
                    maxLines = 1,
                )
            }
        }

        else -> {
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
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadTheme {
        HomeScreen()
    }
}
