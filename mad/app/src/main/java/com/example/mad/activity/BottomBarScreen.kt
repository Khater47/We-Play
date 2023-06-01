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
        route = "profile?userId={userId}",
        title="Profile",
        icon= Icons.Default.Person
    )
    object ProfileEdit:BottomBarScreen(
        route = "profileEdit/{userId}",
        title="Edit Profile",
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

    object SearchField:BottomBarScreen(
        route = "searchField",
        title="Search Field",
        icon= Icons.Default.CarRental
    )

    object AddRating:BottomBarScreen(
        route = "addProfileRating",
        title="Rating Playground",
        icon= Icons.Default.Sports
    )
    object EditReservation: BottomBarScreen(
        route = "editReservation/{reservationId}",
        title="Edit Reservation",
        icon= Icons.Default.Sports
    )

    object Login: BottomBarScreen(
        route = "login",
        title="Login",
        icon= Icons.Default.Sports
    )

    object Playground: BottomBarScreen(
        route = "playground",
        title="Playground",
        icon= Icons.Default.Sports
    )

    object Registration: BottomBarScreen(
        route = "registration",
        title="Registration",
        icon= Icons.Default.Sports
    )

    object Notifications: BottomBarScreen(
        route = "notifications",
        title="Notifications",
        icon= Icons.Default.Sports
    )



}
