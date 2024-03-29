package com.example.mad.activity

//import com.example.mad.screens.reservation.EditReservationScreen
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.example.mad.MainViewModel
import com.example.mad.screens.home.HomeScreen
import com.example.mad.screens.home.LoginScreen
import com.example.mad.screens.home.NotificationScreen
import com.example.mad.screens.home.RegistrationScreen
import com.example.mad.screens.profile.ProfileEditScreen
import com.example.mad.screens.profile.ProfileRatingScreen
import com.example.mad.screens.profile.ProfileScreen
import com.example.mad.screens.profile.ProfileSportScreen
import com.example.mad.screens.rentField.PlaygroundScreen
import com.example.mad.screens.rentField.SearchPlaygroundScreen
import com.example.mad.screens.reservation.ReservationScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavGraph(navController: NavHostController, vm: MainViewModel) {


    val startDestination = if (vm.currentUser.value?.uid != null) BottomBarScreen.Home.route
    else BottomBarScreen.Login.route

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(vm, navController)
        }
        composable(route = BottomBarScreen.Registration.route) {
            RegistrationScreen(vm,navController)
        }
        composable(
            route = BottomBarScreen.Playground.route + "/{playgroundId}",
            arguments = listOf(navArgument("playgroundId") { defaultValue = "0" })
        ) { backStackEntry ->
            PlaygroundScreen(
                vm,
                navController,
                backStackEntry.arguments?.getString("playgroundId")
            )
        }
        composable(route = BottomBarScreen.Reservation.route) {
            ReservationScreen(vm)
        }
        composable(route = BottomBarScreen.ProfileSport.route) {
            ProfileSportScreen(navController, vm)
        }
        composable(
            route = BottomBarScreen.ProfileEdit.route,
        ) {
            ProfileEditScreen(navController, vm)
        }
        composable(route = BottomBarScreen.ProfileRating.route) {
            ProfileRatingScreen(navController, vm)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController, vm)
        }
        composable(route = BottomBarScreen.SearchField.route) {
            SearchPlaygroundScreen(navController, vm)
        }

        composable(
            route = BottomBarScreen.Login.route,
        ) {
            LoginScreen(vm, navController)
        }

        composable(route = BottomBarScreen.Notifications.route) {
            NotificationScreen(navController,vm)
        }

    }
}

//
//arguments = listOf(navArgument("userId") { type = NavType.StringType })
//backStackEntry ->
//ProfileScreen(navController,vm/*navController, vm, backStackEntry.arguments?.getString("userId")*/)