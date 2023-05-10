package com.example.mad.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val equipment:Int,
    val idPlayground: Int,
    val idStartTimeSlot: Int,
    val idEndTimeSlot: Int,
    val idProfile:Int,
)