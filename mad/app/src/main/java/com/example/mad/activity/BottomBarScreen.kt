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
        route = "${Graph.MAIN}/home",
        title="Home",
        icon= Icons.Default.Home
    )

    object Profile:BottomBarScreen(
        route = "${Graph.MAIN}/profile?userId={userId}",
        title="Profile",
        icon= Icons.Default.Person
    )
    object ProfileEdit:BottomBarScreen(
        route = "${Graph.MAIN}/profileEdit/{userId}",
        title="Edit Profile",
        icon= Icons.Default.Person
    )

    object ProfileSport:BottomBarScreen(
        route = "${Graph.MAIN}/profileSport",
        title="Profile Sport",
        icon= Icons.Default.Sports
    )
    object ProfileRating:BottomBarScreen(
        route = "${Graph.MAIN}/profileRating",
        title="Profile Rating",
        icon= Icons.Default.ThumbUp
    )
    object Reservation:BottomBarScreen(
        route = "${Graph.MAIN}/reservations",
        title="Reservation",
        icon= Icons.Default.CalendarMonth
    )

    object SearchField:BottomBarScreen(
        route = "${Graph.MAIN}/searchField",
        title="Search Field",
        icon= Icons.Default.CarRental
    )

    object AddRating:BottomBarScreen(
        route = "${Graph.MAIN}/addProfileRating",
        title="Rating Playground",
        icon= Icons.Default.Sports
    )
    object EditReservation: BottomBarScreen(
        route = "${Graph.MAIN}/editReservation/{reservationId}",
        title="Edit Reservation",
        icon= Icons.Default.Sports
    )

    object Login: BottomBarScreen(
        route = "login",
        title="Login",
        icon= Icons.Default.Sports
    )

    object Playground: BottomBarScreen(
        route = "${Graph.MAIN}/playground",
        title="Playground",
        icon= Icons.Default.Sports
    )


}
