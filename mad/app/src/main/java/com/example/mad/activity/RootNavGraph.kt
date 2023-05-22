package com.example.mad.activity


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraphBuilder

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.screens.SplashScreen
import com.example.mad.screens.home.LoginScreen
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RootNavigationGraph(navController: NavHostController,vm:MainViewModel){
    NavHost(
        navController=navController,
        route = Graph.ROOT,
        startDestination = Graph.SPLASH_SCREEN
    ){
        composable(route=Graph.SPLASH_SCREEN){
            SplashScreen(navController,vm)
        }

        authNavGraph(navController,vm)

        composable(route=Graph.MAIN){
            MainScreen(vm,navController)

        }


    }
}


object Graph{
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
    const val SPLASH_SCREEN = "splash_screen"
}

