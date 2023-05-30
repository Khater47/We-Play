package com.example.mad.screens.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CircularProgressBar
import com.example.mad.common.composable.PasswordTextField
import com.example.mad.common.composable.TextBasicTitle
import com.example.mad.common.composable.TextFieldDefaultIcon
import com.example.mad.common.validationTextField
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LoginScreen(
    vm: MainViewModel,
    navController: NavHostController
) {

    val (email, setEmail) = remember {
        //mariorossi@gmail.com
        mutableStateOf("mariorossi@gmail.com")
    }
    val (password, setPassword) = remember {
        //password123
        mutableStateOf("password123")
    }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val loading = vm.loadingProgressBar.value

    fun login() {
        val emailValid = validationTextField("email", email)
        val passwordValid = validationTextField("password", password)

        if (!emailValid || !passwordValid) {
            scope.launch {
                snackBarHostState.showSnackbar(
                    "Invalid email or password"
                )
            }
        } else {
            vm.logIn(email,password)
        }
    }

    if(vm.currentUser.value?.uid!=null){
        navController.navigate(BottomBarScreen.Home.route)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) {
        Box(modifier = Modifier.padding(it)){
            Column {
                when (LocalConfiguration.current.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Column(
                                Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {

                                Text(
                                    text = "Login",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "Please sign in to continue",
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }


                            Column(
                                Modifier.weight(2f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {

                                TextFieldDefaultIcon(email, setEmail, R.string.email)

                                Spacer(Modifier.padding(vertical = 5.dp))

                                PasswordTextField(password, setPassword)

                                Spacer(Modifier.padding(vertical = 5.dp))

                                Button(
                                    onClick = { login() },
                                    modifier = Modifier.width(280.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    TextBasicTitle(text = "Login")
                                }

                            }

                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier=Modifier.padding(vertical=20.dp)){
                                Text(text = "Don't have an account?",fontSize=18.sp,style=MaterialTheme.typography.bodyMedium)
                                Text(text = "Signup here",fontSize=18.sp,style= TextStyle(textDecoration = TextDecoration.Underline),
                                    color = Color.Blue,modifier= Modifier
                                        .padding(start = 5.dp)
                                        .clickable {
                                        navController.navigate(BottomBarScreen.Registration.route)
                                        })

                            }


                        }
                    }

                    else -> {
                        Column(Modifier.fillMaxSize()) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column(
                                    Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        text = "Login",
                                        style = MaterialTheme.typography.displaySmall,
                                    )
                                    Text(
                                        text = "Please sign in to continue",
                                        color = MaterialTheme.colorScheme.outline,
                                    )
                                }
                                Column(
                                    Modifier.weight(1.8f), verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Spacer(Modifier.padding(vertical = 20.dp))

                                    TextFieldDefaultIcon(email, setEmail, R.string.email)

                                    Spacer(Modifier.padding(vertical = 15.dp))

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

                                        Button(
                                            onClick = { login() },
                                            modifier = Modifier.width(280.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            ),
                                            shape = RoundedCornerShape(5.dp)
                                        ) {
                                            TextBasicTitle(text = "Login")
                                        }

                                    }

                                }

                            }
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp)
                            ){
                                Text(text = "Don't have an account?",fontSize=18.sp,style=MaterialTheme.typography.bodyMedium)
                                Text(text = "Signup here",fontSize=18.sp,style= TextStyle(textDecoration = TextDecoration.Underline),
                                    color = Color.Blue,modifier=Modifier.padding(start=5.dp)
                                        .clickable {
                                           navController.navigate(BottomBarScreen.Registration.route)

                                        })

                            }
                        }


                    }
                }

            }

            CircularProgressBar(isDisplayed = loading)
        }

    }

}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewLogin() {
//
//    val text = "Email or password wrong"
//
//    MadTheme(darkTheme = false) {
//        LoginScreen()
//    }
//}

//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    device = "spec:width=411dp,height=891dp,orientation=landscape"
//)
//@Composable
//fun DefaultPreviewLoginLandscape() {
//    MadTheme {
//        LoginScreen()
//    }
//}