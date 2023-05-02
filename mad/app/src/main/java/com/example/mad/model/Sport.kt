package com.example.mad.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sports")
data class Sport(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val sport: String,
)