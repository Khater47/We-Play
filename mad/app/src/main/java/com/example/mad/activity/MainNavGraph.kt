package com.example.mad.activity

import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.example.mad.MainViewModel
import com.example.mad.screens.SplashScreen
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
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavGraph(navController: NavHostController,vm:MainViewModel) {

    AnimatedNavHost(navController = navController,
        startDestination = BottomBarScreen.Reservation.route,
    ) {
        composable(route=BottomBarScreen.SplashScreen.route){
            SplashScreen(navController,vm)
        }
        composable(route=BottomBarScreen.Home.route){
            HomeScreen(navController)
        }
        composable(route = BottomBarScreen.Playground.route) {
            PlaygroundScreen()
        }
        composable(route = BottomBarScreen.Reservation.route,) {
            ReservationScreen(navController,vm)
        }
        composable(route = BottomBarScreen.ProfileSport.route) {
            ProfileSportScreen(navController/*navController, vm*/)
        }
        composable(
            route = BottomBarScreen.ProfileEdit.route,
        ) {
            ProfileEditScreen(navController /*vm, navController, */)
        }
        composable(route=BottomBarScreen.ProfileRating.route){
            ProfileRatingScreen(navController/*navController,vm*/)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController,vm/*navController, vm, */)
        }
        composable(route = BottomBarScreen.SearchField.route) {
            SearchPlaygroundScreen(navController/*navController,vm*/)
        }
        composable(route = BottomBarScreen.AddRating.route) {
            AddRatingScreen(navController/*navController,vm*/)
        }
        composable(route=BottomBarScreen.EditReservation.route,
            arguments = listOf(navArgument("reservationId") { defaultValue = "0" })){
                backStackEntry -> EditReservationScreen(navController/*navController, vm, backStackEntry.arguments?.getString("reservationId")*/)
        }
        composable(route = BottomBarScreen.Login.route,
        ) {
            LoginScreen(vm,navController)
        }

    }
}

//
//arguments = listOf(navArgument("userId") { type = NavType.StringType })
//backStackEntry ->
//ProfileScreen(navController,vm/*navController, vm, backStackEntry.arguments?.getString("userId")*/)