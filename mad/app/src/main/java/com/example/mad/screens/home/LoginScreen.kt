package com.example.mad.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mad.R
import com.example.mad.common.composable.ButtonBasic
import com.example.mad.common.composable.TextFieldDefaultIcon
import com.example.mad.ui.theme.MadTheme

@Composable
fun LoginScreen(
//    viewModel: MainViewModel
) {

    val (email,setEmail) = remember {
        mutableStateOf("mariorossi@gmail.com")
    }
    val (password,setPassword) = remember {
        mutableStateOf("password123")
    }

    fun login(){
//        viewModel.onSignInClick(email,password)
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Login Icon"
        )

        TextFieldDefaultIcon(email, setEmail , R.string.email)

        TextFieldDefaultIcon(password, setPassword , R.string.password)


        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,modifier= Modifier
                .padding(vertical = 10.dp)
                .clip(
                    RoundedCornerShape(10.dp)
                )
        ) {

            ButtonBasic(R.string.login,::login )

        }

    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreviewLogin() {

    MadTheme {
        LoginScreen()
    }
}

@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
@Composable
fun DefaultPreviewLoginLandscape() {
    MadTheme {
        LoginScreen()
    }
}