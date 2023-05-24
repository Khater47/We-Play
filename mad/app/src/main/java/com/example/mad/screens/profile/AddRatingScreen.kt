package com.example.mad.screens.profile

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CardPlayground
import com.example.mad.common.composable.DefaultImage
import com.example.mad.common.composable.IconButtonRating
import com.example.mad.common.composable.TextBasicBody
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextBasicIcon
import com.example.mad.common.composable.TextBasicTitle
import com.example.mad.common.composable.TopBarComplete
import com.example.mad.common.getIconPlayground
import com.example.mad.common.getIconSport
import com.example.mad.model.Playground
import com.example.mad.ui.theme.MadTheme


@Composable
fun AddRatingScreen(
    navController: NavHostController,
//    vm: MainViewModel
) {
    fun goToPreviousPage() {
        navController.navigate(BottomBarScreen.ProfileRating.route)
    }

    fun saveData() {
        //viewModel stuff
    }

    val p = Playground("Campo Admonds", "Soccer", "Turin")
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
            action = ::saveData
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

        Column(Modifier.weight(1f)) {
            CardPlayground(
                playground
            )
        }
        Column(Modifier.weight(1.2f)) {
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
        modifier = Modifier.padding(horizontal = 10.dp).fillMaxSize()
    ) {
        Column(Modifier.weight(1f)) {
            CardPlayground(playground)
        }
        Column(
            Modifier.weight(1f),
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
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(text=text,fontSize=20.sp,style=MaterialTheme.typography.bodyMedium)
                }
                Column(Modifier.weight(1.5f)) {
                    IconButtonRating(score, setScore)
                }
            }
        }

        else -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                TextBasicHeadLine(text = text)

            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                IconButtonRating(score, setScore)
            }
        }
    }
}

//Portrait Preview
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewAddRating() {
//    MadTheme {
//        AddRatingScreen()
//    }
//}
//
//
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewAddRatingLandscape() {
//    MadTheme {
//        AddRatingScreen()
//    }
//}
