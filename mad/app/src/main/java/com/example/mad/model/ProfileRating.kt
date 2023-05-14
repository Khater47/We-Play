package com.example.mad.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profileRating")
data class ProfileRating(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val quality: Int?,
    val facilities: Int?,
    val idProfile: Int,
    val idPlayground:Int,
    )