package com.example.mad.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val fullName: String,
    val email: String,
    val nickname:String,
    val description:String,
    val phone:String,
)