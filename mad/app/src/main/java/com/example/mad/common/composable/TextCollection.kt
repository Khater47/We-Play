package com.example.mad.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextTopBar(@StringRes id:Int){
    Text(
        text = stringResource(id),
        color = Color.White,
        fontSize = 24.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun TextBasicHeadLine(text:String){

    val headlineSmall = MaterialTheme.typography.headlineSmall
    Text(text,style=headlineSmall)
}

@Composable
fun TextBasicTitle(text:String){

    val titleSmall = MaterialTheme.typography.titleSmall
    Text(text,style=titleSmall)
}



@Composable
fun TextBasicIcon(text:String,icon:ImageVector){

    val bodyMedium = MaterialTheme.typography.bodyMedium
    Row(verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center){
        Icon(icon,contentDescription = "iconLabel",modifier=Modifier.padding(horizontal=10.dp))
        Text(text,style=bodyMedium,fontSize=18.sp)
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewText() {
//    val id = R.string.appName
//
//    MadTheme {
//        Column {
//            TextBasicDisplay(id)
//            Spacer(modifier = Modifier.padding(vertical=20.dp))
//            TextBasicHeadLine(id)
//            Spacer(modifier = Modifier.padding(vertical=20.dp))
//            TextBasicTitle(id)
//            Spacer(modifier = Modifier.padding(vertical=20.dp))
//            TextBasicBody(id)
//            Spacer(modifier = Modifier.padding(vertical=20.dp))
//            TextBasicLabel(id)
//            Spacer(modifier = Modifier.padding(vertical=20.dp))
//            TextBasicIcon(id, Icons.Default.SportsSoccer)
//        }
//    }
//}
