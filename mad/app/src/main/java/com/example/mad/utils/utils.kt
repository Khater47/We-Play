package com.example.mad.utils

import androidx.compose.material.icons.Icons
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
