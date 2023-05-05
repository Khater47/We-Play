package com.example.mad.activity


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sports
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route:String,
    val title:String,
    val icon: ImageVector
){
    object Home:BottomBarScreen(
        route = "home",
        title="Home",
        icon= Icons.Default.Home
    )

    object Profile:BottomBarScreen(
        route = "profile",
        title="Profile",
        icon= Icons.Default.Person
    )

    object Reservation:BottomBarScreen(
        route = "reservations",
        title="Reservation",
        icon= Icons.Default.CalendarMonth
    )

    object Sport:BottomBarScreen(
        route = "sport",
        title="Sport",
        icon= Icons.Default.Sports
    )
}
