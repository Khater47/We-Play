package com.example.mad.activity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mad.MainViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen(vm:MainViewModel) {

    val navController = rememberAnimatedNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        Box(Modifier.padding(it)) {
            MainNavGraph(navController,vm)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route!=BottomBarScreen.Login.route){

        val screens = listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Reservation,
            BottomBarScreen.Profile,
        )
        BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary) {
            screens.forEach { screen ->
                AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
            }
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController


) {
    currentDestination?.hierarchy?.let { s ->
        BottomNavigationItem(label = {
            Text(text = screen.title)
        },
            icon = { Icon(imageVector = screen.icon, contentDescription = "Navigation Icon") },
            selected = s.any { navDestination ->
                navDestination.route == screen.route
            },
            onClick = {
                navController.navigate(screen.route)/*{

                    Avoid multiple copies of the same destination when re-selecting the same item
                    launchSingleTop = true
                    Restore state when re-selecting a previously selected item
                    restoreState = true
                }*/
            }

        )
    }
}