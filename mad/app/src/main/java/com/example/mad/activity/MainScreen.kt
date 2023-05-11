package com.example.mad.activity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mad.UserViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainScreen(vm:UserViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController)}
    ) {
        Box(Modifier.padding(it)) {
            BottomNavGraph(navController = navController,vm)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Reservation,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach { screen ->
            AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
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
                navController.navigate(screen.route){
                    //Pop up to the start destination of the graph to avoid
                    //building up a large stack of destinations on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id){
                        saveState=true
                    }
                    //Avoid multiple copies of the same destination when re-selecting the same item
                    launchSingleTop = true
                    //Restore state when re-selecting a previously selected item
                    restoreState = true
                }
            }

        )
    }
}