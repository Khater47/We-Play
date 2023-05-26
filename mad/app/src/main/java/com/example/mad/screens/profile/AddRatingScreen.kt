package com.example.mad.screens.profile

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CardPlayground
import com.example.mad.common.composable.IconButtonRating
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TopBarComplete
import com.example.mad.model.Playground
import com.example.mad.ui.theme.MadTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRatingScreen(
    navController: NavHostController,
//    vm: MainViewModel
) {


    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(text = "Are you sure you want to save?")
                    Row(modifier = Modifier.fillMaxWidth()) {


                        TextButton(onClick = { openDialog.value = false }) {
                            Text(text = "Back")

                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = { saveData() })
                        {
                            Text(text = "Confirm")
                        }
                    }

                }
            }
        }
    }

    fun goToPreviousPage() {
        navController.navigate(BottomBarScreen.ProfileRating.route)
    }



    fun openDialog() {
        openDialog.value = true
    }

    val p = Playground("Campo Admonds", "Soccer", "Corso Einaudi 24,10129","Turin")
    val (quality, setQuality) = remember {
        mutableStateOf(0)
    }
    val (facilities, setFacilities) = remember {
        mutableStateOf(0)
    }

    val orientation = LocalConfiguration.current.orientation

    Scaffold(topBar = {
        TopBarComplete(
            id = R.string.topBarUserRating,
            icon = Icons.Default.Check,
            backAction = ::goToPreviousPage,
            action = ::openDialog
        )
    }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    RatingPortrait(p, quality, setQuality, facilities, setFacilities)
                }

                else -> {
                    RatingLandscape(p, quality, setQuality, facilities, setFacilities)
                }
            }
        }
    }

}

fun saveData() {
    Log.d("salvato", "SAAAAAAAAAAAA")
}


@Composable
fun RatingPortrait(
    playground: Playground,
    quality: Int,
    setQuality: (Int) -> Unit,
    facilities: Int,
    setFacilities: (Int) -> Unit,
) {


    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Column(Modifier.weight(2f)) {
            CardPlayground(
                playground
            )
        }
        Column(Modifier.weight(3f)) {
            RatingRow(
                quality,
                setQuality,
                "Quality"

            )

            RatingRow(
                facilities,
                setFacilities,
                "Facilities"
            )

            RatingTextSection(text = "")




            //Comment field
        }

    }

}

@Composable
fun RatingLandscape(
    playground: Playground,
    quality: Int,
    setQuality: (Int) -> Unit,
    facilities: Int,
    setFacilities: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize()
    ) {
        Column(Modifier.weight(2f)) {
            CardPlayground(playground)
        }
        Column(
            Modifier
                .weight(4f)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            RatingRow(
                quality,
                setQuality,
                "Quality"

            )

            RatingRow(
                facilities,
                setFacilities,
                "Facilities"
            )

            RatingTextSection(text = "")
        }

    }
}




@Composable
fun RatingRow(score: Int, setScore: (Int) -> Unit, text: String) {
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    TextBasicHeadLine(text = text)
                }
                Column(Modifier.weight(2f)) {
                    IconButtonRating(score, setScore)
                }
            }
        }

        else -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    TextBasicHeadLine(text = text)
                }
                Column(Modifier.weight(1f)) {
                    IconButtonRating(score, setScore)
                }
            }
        }
    }
}

@Composable
fun RatingTextSection(text : String) {

    var text by rememberSaveable { mutableStateOf("") }

    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(value = text,
                    onValueChange = { text = it},
                    label = {Text("Comment Section")},

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .fillMaxHeight()
                        .background(Color.White)


                )
            }
        }

        else -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 5.dp)
            ) {

                OutlinedTextField(value = text,
                    onValueChange = { text = it},
                    label = {Text("Comment Section")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .fillMaxHeight()
                        .background(Color.White)


                )


            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreviewAddRating() {
    MadTheme {
        AddRatingScreen(rememberNavController())
    }
}

//
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewAddRatingLandscape() {
//    MadTheme {
//        RatingTextSection(text = "")
//    }
//
//}
