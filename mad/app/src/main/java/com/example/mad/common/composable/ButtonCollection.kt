package com.example.mad.common.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad.R
import com.example.mad.ui.theme.Gold


@Composable
fun FloatingButtonAdd(action: ()->Unit){

    FloatingActionButton(
        onClick = { action() },
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.clip(CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun ConfirmAlertButton(onClick:()->Unit){
    Button(onClick = {onClick()}, modifier = Modifier.clip(RectangleShape)) {
        Text("GRANT PERMISSION")
    }
}



@Composable
fun DismissAlertButton(onDismiss:()->Unit){
    Button(
        onClick = { onDismiss() },
        Modifier.clip(RectangleShape)
    ) {
        Text("CANCEL")
    }
}

@Composable
fun ButtonDialog(
    cancel: () -> Unit,
    confirm: () -> Unit
) {

    val confirmText = stringResource(id = R.string.confirm)
    val cancelText = stringResource(id = R.string.cancel)

    Row(
        Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()) {
        Column(
            Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            ButtonBasic(R.string.cancel, action = cancel)
            Button(onClick = { cancel() }, colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.outline,
                contentColor = MaterialTheme.colorScheme.onBackground
            )) {
                Text(text = cancelText,fontSize=18.sp,style=MaterialTheme.typography.bodyMedium)
            }
        }
        Column(
            Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            ButtonBasic(R.string.confirm, action = confirm)
            Button(onClick = { confirm() }, colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )) {
                Text(text = confirmText,fontSize=18.sp,style=MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun IconButtonDelete(action: () -> Unit) {
    IconButton(onClick = { action() }) {
        Icon(Icons.Default.Delete,tint=MaterialTheme.colorScheme.error, contentDescription = "deleteButton")
    }
}


@Composable
fun IconButtonRating(
    score: Int,
    setScore: (Int) -> Unit
) {

    LazyRow {
        items(5) { index ->
            IconButton(onClick = {
                setScore(index + 1)
            }) {
                Icon(
                    Icons.Default.Star, contentDescription = "starIcon",
                    tint = if (index < score) Gold else Color.Gray
                )
            }

        }
    }

}

@Composable
fun Score(score: Long) {
    LazyRow {
        items(5) { index ->
            Icon(
                Icons.Default.Star, contentDescription = "starIcon",
                tint = if (index < score) Gold else Color.Gray
            )

        }
    }
}

@Composable
fun NavigationIconButtonTopBar(
    backAction: () -> Unit
) {
    IconButton(onClick = {
        backAction()
    }) {
        Icon(Icons.Filled.ArrowBack, "backIcon")
    }
}

@Composable
fun ActionButtonTopBar(
    icon: ImageVector,
    action: () -> Unit
) {
    IconButton(onClick = {
        action()
    }) {
        Icon(icon, "actionIcon")
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewButton() {
//    val id = R.string.confirm
//    fun action() {}
//    val (score, setScore) = remember {
//        mutableStateOf(3)
//    }
//
//    MadTheme {
//
//        Column {
//            ButtonBasic(id, ::action)
//            Spacer(Modifier.padding(vertical = 10.dp))
//            ButtonDialog(::action, ::action)
//            Spacer(Modifier.padding(vertical = 10.dp))
//            IconButtonDelete(::action)
//            Spacer(Modifier.padding(vertical = 10.dp))
//            IconButtonEdit(::action)
//            Spacer(Modifier.padding(vertical = 10.dp))
//            IconButtonRating(score, setScore)
//            Spacer(Modifier.padding(vertical = 10.dp))
//            Score(score)
//
//        }
//    }
//}
