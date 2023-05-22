package com.example.mad.activity

import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mad.MainViewModel
import com.example.mad.screens.home.HomeScreen
import com.example.mad.screens.profile.AddRatingScreen
import com.example.mad.screens.profile.ProfileEditScreen
import com.example.mad.screens.profile.ProfileRatingScreen
import com.example.mad.screens.profile.ProfileScreen
import com.example.mad.screens.profile.ProfileSportScreen
import com.example.mad.screens.rentField.PlaygroundScreen
import com.example.mad.screens.rentField.SearchPlaygroundScreen
import com.example.mad.screens.reservation.EditReservationScreen
import com.example.mad.screens.reservation.ReservationScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavGraph(navController: NavHostController,vm:MainViewModel,rootNavController:NavHostController) {

    NavHost(navController = navController,
        startDestination = BottomBarScreen.Profile.route,
    ) {
        composable(route=BottomBarScreen.Home.route){
            HomeScreen(navController)
        }
        composable(route = BottomBarScreen.Playground.route) {
            PlaygroundScreen()
        }
        composable(route = BottomBarScreen.Reservation.route) {
            ReservationScreen(navController/*navController,vm*/)
        }
        composable(route = BottomBarScreen.ProfileSport.route) {
            ProfileSportScreen(navController/*navController, vm*/)
        }
        composable(
            route = BottomBarScreen.ProfileEdit.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            ProfileEditScreen(navController /*vm, navController, backStackEntry.arguments?.getString("userId")*/)
        }
        composable(route=BottomBarScreen.ProfileRating.route){
            ProfileRatingScreen(navController/*navController,vm*/)
        }
        composable(
            route = BottomBarScreen.Profile.route,
            arguments = listOf(navArgument("userId") { defaultValue = "2" })
        ) { backStackEntry ->
            ProfileScreen(navController,vm,rootNavController/*navController, vm, backStackEntry.arguments?.getString("userId")*/)
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

    }
}
