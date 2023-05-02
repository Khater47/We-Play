package com.example.mad.profile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.mad.ui.theme.MadTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import com.example.mad.R
import java.time.format.TextStyle

@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = { TopAppBarProfile() }
    ) {
        Box(Modifier.padding(it)) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),

                ) {
                UserImage()
                Spacer(modifier = Modifier.padding(2.dp))
                ContentUserProfile()

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
                Icon(Icons.Default.Edit, "Edit",Modifier.size(28.dp))
            }
        }

    )
}

@Composable
fun UserImage() {

    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
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
        Text(text = "mariorossi@gmail.com", fontSize = 20.sp)

    }

}

@Composable
fun ContentUserProfile() {

    val body = MaterialTheme.typography.body1
    val modifierText = Modifier.padding(vertical = 15.dp)

    Column(
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {


        Text(text = "Personal Info", modifier = modifierText, style = body, fontSize = 20.sp)
        Divider()

        LabelText("Nickname", Icons.Outlined.AlternateEmail)
        Text(text = "mario", modifier = modifierText, style = body, fontSize = 18.sp)
        Divider()

        LabelText("Phone",Icons.Default.Phone)
        Text(text = "1234567890", modifier = modifierText, style = body, fontSize = 18.sp)
        Divider()

        LabelText("Description",Icons.Default.Description)
        Text(text = "student", modifier = modifierText, style = body, fontSize = 18.sp)

    }


}

@Composable
fun LabelText(text:String,icon:ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column() {
            Icon(icon, text)
        }
        Column(modifier = Modifier.padding(horizontal = 5.dp)) {
            Text(text,modifier=Modifier.padding(vertical = 15.dp),style=MaterialTheme.typography.body1,fontSize=18.sp)

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

