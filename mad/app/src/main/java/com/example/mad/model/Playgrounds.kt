package com.example.mad.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playgrounds")
data class Playgrounds(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val sport: String,
    val playgrounds:String,
    val location:String,
)