package com.example.mad.activity


import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mad.home.HomeScreen
import com.example.mad.profile.ProfileScreen
import com.example.mad.reservation.ReservationScreen
import com.example.mad.sport.SportScreen


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route=BottomBarScreen.Home.route){
            HomeScreen()
        }
        composable(route=BottomBarScreen.Reservation.route){
            ReservationScreen()
        }
        composable(route=BottomBarScreen.Sport.route){
            SportScreen()
        }
        composable(route=BottomBarScreen.Profile.route){
            ProfileScreen()
        }

    }
}