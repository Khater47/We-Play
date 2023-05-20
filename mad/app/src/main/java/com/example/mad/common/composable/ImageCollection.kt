package com.example.mad.common.composable

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mad.R
import com.example.mad.common.getIconPlayground
import com.example.mad.ui.theme.MadTheme


@Composable
fun ImageCardHome(icon: ImageVector, image: Int) {

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
fun CircleImage(image: ImageBitmap) {

    Image(
        bitmap = image,
        contentDescription = "Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)

    )
}

@Composable
fun DefaultImage(image: String) {

    Image(
        painter = painterResource(id = getIconPlayground(image)),
        contentDescription = "Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
    )
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//
//    MadTheme {
//        ImageCardHome()
//    }
//}