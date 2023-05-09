package com.example.mad.activity


import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mad.UserViewModel
import com.example.mad.home.HomeScreen
import com.example.mad.profile.ProfileScreen
import com.example.mad.profileEdit.ProfileEditScreen
import com.example.mad.profileRating.ProfileRatingScreen
import com.example.mad.reservation.ReservationScreen
import com.example.mad.profileSport.ProfileSportScreen
import com.example.mad.rentField.RentFieldScreen
import com.example.mad.sport.SportScreen


@Composable
fun BottomNavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = BottomBarScreen.ProfileEdit.route) {
        composable(route=BottomBarScreen.Home.route){
            HomeScreen(navController)
        }
        composable(route=BottomBarScreen.Reservation.route){
            ReservationScreen()
        }
        composable(route=BottomBarScreen.ProfileSport.route){
            ProfileSportScreen()
        }
        composable(route=BottomBarScreen.ProfileEdit.route){
            ProfileEditScreen()
        }
        composable(route=BottomBarScreen.ProfileRating.route){
            ProfileRatingScreen()
        }
        composable(route=BottomBarScreen.Profile.route){
            ProfileScreen(navController)
        }
        composable(route=BottomBarScreen.RentField.route){
            RentFieldScreen()
        }
        composable(route=BottomBarScreen.Sport.route){
            SportScreen()
        }

    }
}