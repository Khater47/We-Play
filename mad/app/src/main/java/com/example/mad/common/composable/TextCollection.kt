package com.example.mad.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad.R
import com.example.mad.ui.theme.MadTheme

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
fun TextBasicDisplay(text:String){

    val displaySmall = MaterialTheme.typography.displaySmall

    Text(text,style=displaySmall)
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
fun TextBasicBody(text:String){

    val bodySmall = MaterialTheme.typography.bodySmall
    Text(text,style=bodySmall)
}

@Composable
fun TextBasicLabel(text:String){

    val labelSmall = MaterialTheme.typography.labelSmall
    Text(text,style=labelSmall)
}

@Composable
fun TextBasicIcon(text:String,icon:ImageVector){

    val bodyMedium = MaterialTheme.typography.bodyMedium
    Row(verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center){
        Icon(icon,contentDescription = "iconLabel",modifier=Modifier.padding(horizontal=10.dp))
        Text(text,style=bodyMedium)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreviewText() {
    val id = R.string.appName

    MadTheme {
        Column {
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
        }
    }
}
