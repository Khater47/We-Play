package com.example.mad.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimeSlotDao {

    @Query("SELECT * FROM timeSlot ORDER BY id ASC")
    fun getAll() : LiveData<List<TimeSlot>>

    @Query("SELECT * FROM timeSlot WHERE time=:time")
    fun getTimeSlotByTime(time:String) : LiveData<TimeSlot>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeSlot(timeSlot: TimeSlot)

    @Delete
    suspend fun deleteTimeSlot(timeSlot: TimeSlot)

}