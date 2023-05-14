package com.example.mad.activity


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mad.UserViewModel
import com.example.mad.home.HomeScreen
import com.example.mad.profile.ProfileScreen
import com.example.mad.profileEdit.ProfileEditScreen
import com.example.mad.profileRating.ProfileRatingScreen
import com.example.mad.reservation.ReservationScreen
import com.example.mad.profileSport.ProfileSportScreen
import com.example.mad.rentField.RentFieldScreen
import com.example.mad.sport.SportScreen


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BottomNavGraph(navController: NavHostController,vm:UserViewModel) {

    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route=BottomBarScreen.Home.route){
            HomeScreen(navController)
        }
        composable(route=BottomBarScreen.Reservation.route){
            ReservationScreen()
        }
        composable(route=BottomBarScreen.ProfileSport.route){
            ProfileSportScreen(/*navController,vm*/)
        }
        composable(route=BottomBarScreen.ProfileEdit.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ){ backStackEntry ->
            ProfileEditScreen(vm,navController,backStackEntry.arguments?.getString("userId"))
        }
        composable(route=BottomBarScreen.ProfileRating.route){
            ProfileRatingScreen(navController,vm)
        }
        composable(route=BottomBarScreen.Profile.route,
            arguments = listOf(navArgument("userId") { defaultValue = "2" })
            ){
                backStackEntry ->
            ProfileScreen(navController,vm, backStackEntry.arguments?.getString("userId"))
        }
        composable(route=BottomBarScreen.RentField.route){
            RentFieldScreen()
        }
        composable(route=BottomBarScreen.Sport.route){
            SportScreen()
        }

    }
}