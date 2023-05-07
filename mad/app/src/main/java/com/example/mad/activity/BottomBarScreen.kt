package com.example.mad.activity


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.ThumbUp
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

    object ProfileSport:BottomBarScreen(
        route = "profileSport",
        title="Profile Sport",
        icon= Icons.Default.Sports
    )
    object ProfileRating:BottomBarScreen(
        route = "profileRating",
        title="Profile Rating",
        icon= Icons.Default.ThumbUp
    )
    object Reservation:BottomBarScreen(
        route = "reservations",
        title="Reservation",
        icon= Icons.Default.CalendarMonth
    )

    object RentField:BottomBarScreen(
        route = "rentField",
        title="Rent Field",
        icon= Icons.Default.CarRental
    )

    object Sport:BottomBarScreen(
        route = "sport",
        title="Select Sport",
        icon= Icons.Default.Sports
    )


}