package com.example.mad.screens.home

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.ButtonBasic
import com.example.mad.common.composable.PasswordTextField
import com.example.mad.common.composable.TextFieldDefaultIcon
import com.example.mad.ui.theme.MadTheme

@Composable
fun LoginScreen(
//    vm: MainViewModel,
//    navController: NavHostController
) {

    val (email, setEmail) = remember {
        mutableStateOf("mariorossi@gmail.com")
    }
    val (password, setPassword) = remember {
        mutableStateOf("password123")
    }

    fun login() {
//        Log.d("TAG","$email $password")
//        vm.onSignInClick(email,password)
//        if(vm.currentUser.value!=null)
//            navController.navigate(BottomBarScreen.Home.route)

    }

    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Login Image",
                    modifier = Modifier
                        .size(180.dp)
                        .padding(vertical = 30.dp)
                )

                TextFieldDefaultIcon(email, setEmail, R.string.email)

                Spacer(Modifier.padding(vertical=5.dp))

                PasswordTextField(password, setPassword)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center, modifier = Modifier
                        .padding(vertical = 30.dp)
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                ) {


                    ButtonBasic(R.string.login, ::login)

                }

            }
        }

        else -> {
            Row(Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
                Column(Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Login Image",
                        modifier = Modifier
                            .size(180.dp)
                            .padding(vertical = 30.dp)
                    )
                }
                Column(Modifier.weight(1.8f),  verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Spacer(Modifier.padding(vertical=20.dp))

                    TextFieldDefaultIcon(email, setEmail, R.string.email)

                    Spacer(Modifier.padding(vertical=15.dp))

                    TextFieldDefaultIcon(password, setPassword, R.string.password)


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center, modifier = Modifier
                            .padding(vertical = 30.dp)
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(10.dp)
                            )
                    ) {

                        ButtonBasic(R.string.login, ::login)

                    }

                }
            }
        }
    }


}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreviewLogin() {

    MadTheme {
        LoginScreen()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,orientation=landscape"
)
@Composable
fun DefaultPreviewLoginLandscape() {
    MadTheme {
        LoginScreen()
    }
}