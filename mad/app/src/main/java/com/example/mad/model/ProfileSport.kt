package com.example.mad.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profileSport")
data class ProfileSport(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val achievement: String?,
    val sport: String,
    val level:Int?,
    val idProfile:Int,
)