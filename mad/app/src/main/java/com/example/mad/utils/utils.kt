package com.example.mad.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.SportsGolf
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType

fun getIconSport(sportText:String): ImageVector {
    return when (sportText){
        "Soccer" -> Icons.Default.SportsSoccer
        "Baseball" -> Icons.Default.SportsBaseball
        "Basketball" -> Icons.Default.SportsBasketball
        "Cricket" -> Icons.Default.SportsCricket
        "Football" -> Icons.Default.SportsFootball
        "Golf" -> Icons.Default.SportsGolf
        "Gymnastic" -> Icons.Default.SportsGymnastics
        "Tennis" -> Icons.Default.SportsTennis
        else -> Icons.Default.SportsVolleyball

    }
}

fun getIconUserInfo(userInfo:String): ImageVector{
    return when (userInfo){
        "FullName" -> Icons.Default.Person
        "Email" -> Icons.Default.Email
        "Nickname" -> Icons.Default.AlternateEmail
        "Description" -> Icons.Default.Description
        else -> Icons.Default.Phone

    }
}


fun getKeyboard(userInfo:String):KeyboardType{
    return when (userInfo){
        "PhoneNumber" -> KeyboardType.Number
        "Email" -> KeyboardType.Email
         else -> KeyboardType.Text
    }
}

