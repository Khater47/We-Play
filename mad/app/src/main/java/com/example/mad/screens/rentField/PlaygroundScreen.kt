package com.example.mad.screens.rentField

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mad.DELAY
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.FullDialogPlayground
import com.example.mad.common.composable.TopBarBackButton
import com.example.mad.model.Comment
import com.example.mad.model.Playground
import com.example.mad.ui.theme.confirmation
import kotlinx.coroutines.delay


/*
TODO:
    1) choose image for playground

*/

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlaygroundScreen(
    vm: MainViewModel,
    navController: NavHostController,
    playgroundId: String?
) {

    val loading = vm.loadingProgressBar.value

    val image = R.drawable.field

    val playground = vm.playground.observeAsState().value ?: Playground(
        phone = "",
        email = "",
        openHours = "",
        playground = "",
        sport = "",
        city = "",
        address = ""
    )
    val comments = vm.comments.observeAsState().value?.filterNotNull() ?: emptyList()

    LaunchedEffect(key1 = playgroundId) {
        vm.loadingProgressBar.value=true
        vm.getPlaygroundById(playgroundId ?: "0")
        vm.getPlaygroundComments(playgroundId ?: "0")
        delay(DELAY)
        vm.loadingProgressBar.value=false
    }

    val avg = remember {
        mutableStateOf(0L)
    }
    val totalQuality = remember {
        mutableStateOf(0L)
    }
    val totalFacilities = remember {
        mutableStateOf(0L)
    }

    if (comments.isNotEmpty()) {
        val q = comments.sumOf { it.quality } / comments.size
        val f = comments.sumOf { it.facilities } / comments.size
        totalQuality.value = q
        totalFacilities.value = f
        avg.value = (totalQuality.value + totalFacilities.value) / 2
        Log.d("COMMENTS",comments.size.toString())
    }

    val showDialog = remember { mutableStateOf(false) }

    fun goToPreviousPage() {
        navController.navigate(BottomBarScreen.SearchField.route)
    }

    Scaffold(
        topBar = {
            TopBarBackButton(R.string.topBarPlayground, ::goToPreviousPage)
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    // Field Image
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                // Name and rating
                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(4f)
                            .padding(horizontal = 15.dp)
                    ) {
                        Text(
                            text = playground.playground,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.StarRate, contentDescription = "",
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        Text(
                            text = "${avg.value.toInt()}",
                            fontSize=18.sp
                        )
                    }

                }


                // Information Section
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                ) {
                    // Card content
                    InfoLine(icon = Icons.Default.LocationOn, text = playground.address)
                    Divider(thickness = 1.dp)
                    InfoLine(icon = Icons.Default.Call, text = playground.phone)
                    Divider(thickness = 1.dp)
                    InfoLine(icon = Icons.Default.Mail, text = playground.email)
                    Divider(thickness = 1.dp)
                    InfoLine(icon = Icons.Default.Schedule, text = playground.openHours)
                }

                // Rating card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = CardDefaults.outlinedCardBorder(),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Rating(text = "Quality", score = totalQuality.value.toInt())
                    Rating(text = "Facilities", score = totalFacilities.value.toInt())
                }

                // Users Comments
                UserComments(comments)

                //Divider()

                Column(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Custom date picker
                    OutlinedButton(
                        onClick = { showDialog.value = true },
                        colors = ButtonDefaults.buttonColors(confirmation)
                    )
                    {
                        Text(
                            "Rent Playground",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    }


                    if (showDialog.value) {
                        FullDialogPlayground(openDialog = showDialog, playground, vm)
                    }

                }

            }
            CircularProgressBar(isDisplayed = loading)

        }
    }


}


@Composable
fun InfoLine(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = "Location", modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.padding(horizontal = 12.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium,fontSize=18.sp)
    }
}

@Composable
fun Rating(text: String, score: Int) {
    Row(modifier = Modifier.padding(16.dp)) {
//        TextBasicHeadLine(text = text)
        Text(
            text = text, style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(weight = 1f),
            fontSize=18.sp
        )
        Spacer(modifier = Modifier.padding(horizontal = 20.dp))
        Row(modifier = Modifier.weight(2f)) {
            for (num in 1..score) {
                Icon(
                    imageVector = Icons.Filled.Grade, contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
            }
            for (num in 1..(5 - score)) {
                Icon(
                    imageVector = Icons.Outlined.Grade, contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun UserComments(comments: List<Comment>) {

    val (isExpanded, setIsExpanded) = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 7.dp)
        ) {

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        setIsExpanded(!isExpanded)
                    },
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize=18.sp,
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.padding(horizontal = 30.dp))
                if (isExpanded) {
                    IconButton(onClick = { setIsExpanded(false) }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Expand"
                        )
                    }
                } else {
                    IconButton(onClick = { setIsExpanded(true) }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand"
                        )
                    }
                }

            }


            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    (comments).forEach {
                        CommentCard(comment = it)
                    }
                }
            }

        }

    }

}


@Composable
fun CommentCard(comment: Comment) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "person",
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Top)
        )
        Spacer(modifier = Modifier.padding(horizontal = 3.dp))
        Card(
            border = CardDefaults.outlinedCardBorder(), modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = comment.nickname,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(7.dp)
                )
                Text(
                    text = comment.comment, style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(7.dp)
                )
            }
        }
    }

}



