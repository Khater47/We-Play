package com.example.mad.common.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mad.common.getIconPlayground


@Composable
fun ImageCardHome(icon: ImageVector, image: Int) {

    Box(
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        )
        Box(
            Modifier
                .width(70.dp)
                .padding(start = 20.dp, bottom = 20.dp)
                .height(50.dp)
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