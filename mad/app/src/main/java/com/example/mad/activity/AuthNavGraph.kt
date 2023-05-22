package com.example.mad.activity

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.mad.MainViewModel
import com.example.mad.screens.home.HomeScreen
import com.example.mad.screens.home.LoginScreen
import com.example.mad.screens.profile.AddRatingScreen
import com.example.mad.screens.profile.ProfileEditScreen
import com.example.mad.screens.profile.ProfileRatingScreen
import com.example.mad.screens.profile.ProfileScreen
import com.example.mad.screens.profile.ProfileSportScreen
import com.example.mad.screens.rentField.PlaygroundScreen
import com.example.mad.screens.rentField.SearchPlaygroundScreen
import com.example.mad.screens.reservation.EditReservationScreen
import com.example.mad.screens.reservation.ReservationScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController,vm:MainViewModel){
    navigation(route=Graph.AUTH,
        startDestination = BottomBarScreen.Login.route){
        composable(route = BottomBarScreen.Login.route) {
            LoginScreen(vm,navController)
        }
    }
}


