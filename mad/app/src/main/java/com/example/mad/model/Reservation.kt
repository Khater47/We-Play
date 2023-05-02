package com.example.mad.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val playgrounds:String,
    val date: String,
    val time:String,
    val location:String
)