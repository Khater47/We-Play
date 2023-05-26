package com.example.mad.activity

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
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
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavGraph(navController: NavHostController,vm:MainViewModel) {

    Log.d("TAG_MAIN_NAV_GRAPH USER",vm.currentUser.value?.uid?:"NULL")

//    val startDestination = if(vm.currentUser.value?.uid!=null) BottomBarScreen.Home.route
//    else BottomBarScreen.Login.route
    val startDestination = BottomBarScreen.Home.route

    AnimatedNavHost(navController = navController,
        startDestination = startDestination,
    ) {

        composable(route=BottomBarScreen.Home.route){
            HomeScreen(navController)
        }
        composable(route = BottomBarScreen.Playground.route) {
            PlaygroundScreen()
        }
        composable(route = BottomBarScreen.Reservation.route) {
            ReservationScreen(navController,vm)
        }
        composable(route = BottomBarScreen.ProfileSport.route) {
            ProfileSportScreen(navController,vm)
        }
        composable(
            route = BottomBarScreen.ProfileEdit.route,
        ) {
            ProfileEditScreen(navController,vm)
        }
        composable(route=BottomBarScreen.ProfileRating.route){
            ProfileRatingScreen(navController,vm)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController,vm/*navController, vm, */)
        }
        composable(route = BottomBarScreen.SearchField.route) {
            SearchPlaygroundScreen(navController,vm)
        }
        composable(route = BottomBarScreen.AddRating.route) {
            AddRatingScreen(navController/*navController,vm*/)
        }
        composable(route=BottomBarScreen.EditReservation.route,
            arguments = listOf(navArgument("reservationId") { defaultValue = "0" })){
               /* _ ->*/ EditReservationScreen(navController/*navController, vm, backStackEntry.arguments?.getString("reservationId")*/)
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