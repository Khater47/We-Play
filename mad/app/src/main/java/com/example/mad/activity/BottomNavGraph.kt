package com.example.mad.activity


import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mad.home.HomeScreen
import com.example.mad.profile.ProfileScreen
import com.example.mad.profileRating.ProfileRatingScreen
import com.example.mad.reservation.ReservationScreen
import com.example.mad.profileSport.ProfileSportScreen
import com.example.mad.rentField.RentFieldScreen


@Composable
fun BottomNavGraph(navController: NavHostController) {


    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route=BottomBarScreen.Home.route){
            HomeScreen(navController)
        }
        composable(route=BottomBarScreen.Reservation.route){
            ReservationScreen()
        }
        composable(route=BottomBarScreen.ProfileSport.route){
            ProfileSportScreen()
        }
        composable(route=BottomBarScreen.ProfileRating.route){
            ProfileRatingScreen()
        }
        composable(route=BottomBarScreen.Profile.route){
            ProfileScreen()
        }
        composable(route=BottomBarScreen.RentField.route){
            RentFieldScreen()
        }

    }
}