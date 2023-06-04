package com.example.mad.screens.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.PasswordTextField
import com.example.mad.common.composable.TextBasicTitle
import com.example.mad.common.composable.TextFieldDefaultIcon
import com.example.mad.common.validationRegistration
import com.example.mad.model.Profile

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RegistrationScreen(
    vm: MainViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current.applicationContext

    val (fullname, setFullname) = remember {
        //mariorossi@gmail.com
        mutableStateOf("Mario Rossi")
    }
    val (nickname, setNickname) = remember {
        //password123
        mutableStateOf("mario")
    }
    val (email, setEmail) = remember {
        //mariorossi@gmail.com
        mutableStateOf("mariorossi@gmail.com")
    }
    val (password, setPassword) = remember {
        //password123
        mutableStateOf("password123")
    }


    val (phone, setPhone) = remember {
        //password123
        mutableStateOf("3456789871")
    }

    val (description, setDescription) = remember {
        //password123
        mutableStateOf("student")
    }

//    val loading = vm.loadingProgressBar.value


    fun registration(){

        val p = Profile(
            description,
            fullname,
            nickname,
            phone,
            email,
        )
        if(validationRegistration(p,password)){
            vm.registration(email,password)
            vm.insertUserRegistrationProfile(p)
            navController.navigate(BottomBarScreen.Login.route)
        }
        else {
            Toast.makeText(context,"Invalid field",Toast.LENGTH_SHORT).show()
        }


    }

    Scaffold {
        Box(modifier = Modifier.padding(it)){
            Column() {
                when (LocalConfiguration.current.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Registration",
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Please sign up to continue",
                                color = MaterialTheme.colorScheme.outline
                            )

                            Column(modifier=Modifier.padding(30.dp),horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {

                                TextFieldDefaultIcon(email, setEmail, R.string.email)

                                Spacer(Modifier.padding(vertical = 5.dp))

                                TextFieldDefaultIcon(fullname, setFullname, R.string.fullName)

                                Spacer(Modifier.padding(vertical = 5.dp))

                                TextFieldDefaultIcon(nickname, setNickname, R.string.nickname)

                                Spacer(Modifier.padding(vertical = 5.dp))

                                TextFieldDefaultIcon(phone, setPhone, R.string.phoneNumber)

                                Spacer(Modifier.padding(vertical = 5.dp))

                                TextFieldDefaultIcon(description, setDescription, R.string.description)

                                Spacer(Modifier.padding(vertical = 5.dp))

                                PasswordTextField(password, setPassword)

                                Spacer(Modifier.padding(vertical = 5.dp))


                                Button(
                                    onClick = { registration() },
                                    modifier = Modifier.width(280.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    TextBasicTitle(text = "Sign up")
                                }

                            }


                        }
                    }

                    else -> {
                        Row(
                            Modifier.fillMaxSize(),
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
                                    text = "Registration",
                                    style = MaterialTheme.typography.displaySmall,
                                )
                                Text(
                                    text = "Please sign up to continue",
                                    color = MaterialTheme.colorScheme.outline,
                                )
                            }
                            Column(
                                Modifier
                                    .weight(1.8f)
                                    .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Spacer(Modifier.padding(vertical = 20.dp))

                                TextFieldDefaultIcon(email, setEmail, R.string.email)

                                Spacer(Modifier.padding(vertical = 15.dp))

                                TextFieldDefaultIcon(fullname, setFullname, R.string.fullName)

                                Spacer(Modifier.padding(vertical = 15.dp))

                                TextFieldDefaultIcon(nickname, setNickname, R.string.nickname)

                                Spacer(Modifier.padding(vertical = 15.dp))

                                TextFieldDefaultIcon(phone, setPhone, R.string.phoneNumber)

                                Spacer(Modifier.padding(vertical = 15.dp))

                                TextFieldDefaultIcon(description, setDescription, R.string.description)

                                Spacer(Modifier.padding(vertical = 15.dp))

                                PasswordTextField(password, setPassword)

                                Spacer(Modifier.padding(vertical = 15.dp))

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
                                        onClick = { registration() },
                                        modifier = Modifier.width(280.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        shape = RoundedCornerShape(5.dp)
                                    ) {
                                        TextBasicTitle(text = "Sign up")
                                    }

                                }

                            }
                        }
                    }
                }

            }

//            CircularProgressBar(isDisplayed = loading)
        }

    }
}

//Portrait Preview

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewRegistration() {
//    MadTheme {
//        RegistrationScreen()
//    }
//}


//Landscape Preview

//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        RegistrationScreen()
//    }
//}




