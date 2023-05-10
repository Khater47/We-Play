package com.example.mad.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mad.UserViewModel
import com.example.mad.ui.theme.MadTheme
import dagger.hilt.android.AndroidEntryPoint

/*
* TODO():
*   Splash Screen + Animation
* */

@RequiresApi(Build.VERSION_CODES.P)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(vm)
                }
            }
        }
    }
}


