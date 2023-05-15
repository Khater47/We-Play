package com.example.mad.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "available_playgrounds")
data class AvailablePlayground(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val sport: String,
    val playground:String,
    val date:String,
    val startTime:String,
    val endTime:String,
    val location:String,
)