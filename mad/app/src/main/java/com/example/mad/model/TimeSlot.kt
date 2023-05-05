package com.example.mad.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "timeSlot")
data class TimeSlot(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: String,
)