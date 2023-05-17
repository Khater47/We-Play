package com.example.mad.activity


import android.os.Build
import android.window.SplashScreen
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mad.R
import com.example.mad.UserViewModel
import com.example.mad.profile.AddRatingScreen
import com.example.mad.home.HomeScreen
import com.example.mad.profile.ProfileScreen
import com.example.mad.profile.ProfileEditScreen
import com.example.mad.profile.ProfileRatingScreen
import com.example.mad.reservation.ReservationScreen
import com.example.mad.profile.ProfileSportScreen
import com.example.mad.rentField.RentFieldScreen
import com.example.mad.reservation.EditReservationScreen
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BottomNavGraph(navController: NavHostController, vm: UserViewModel) {

    NavHost(navController = navController, startDestination = "splashScreen") {
        composable(route="splashScreen"){
            SplashScreen(navController)
        }
        composable(route=BottomBarScreen.Home.route){
            HomeScreen(navController)
        }
        composable(route = BottomBarScreen.Reservation.route) {
            ReservationScreen(navController,vm)
        }
        composable(route = BottomBarScreen.ProfileSport.route) {
            ProfileSportScreen(navController, vm)
        }
        composable(
            route = BottomBarScreen.ProfileEdit.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            ProfileEditScreen(vm, navController, backStackEntry.arguments?.getString("userId"))
        }
        composable(route=BottomBarScreen.ProfileRating.route){
            ProfileRatingScreen(navController,vm)
        }
        composable(
            route = BottomBarScreen.Profile.route,
            arguments = listOf(navArgument("userId") { defaultValue = "2" })
        ) { backStackEntry ->
            ProfileScreen(navController, vm, backStackEntry.arguments?.getString("userId"))
        }
        composable(route = BottomBarScreen.RentField.route) {
            RentFieldScreen(navController,vm)
        }
        composable(route = BottomBarScreen.AddRating.route) {
            AddRatingScreen(navController,vm)
        }
        composable(route=BottomBarScreen.EditReservation.route,
            arguments = listOf(navArgument("reservationId") { defaultValue = "0" })){
                backStackEntry -> EditReservationScreen(navController, vm, backStackEntry.arguments?.getString("reservationId"))
        }

    }
}

@Composable
fun SplashScreen(navController: NavHostController){
    LaunchedEffect(key1 = true){
        delay(3000L)
        navController.navigate(BottomBarScreen.Home.route)
    }
    
    Column(
        modifier= Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(painter = painterResource(id = R.drawable.profile),
            contentDescription = "Logo",
        modifier=Modifier.size(200.dp))
    }
}