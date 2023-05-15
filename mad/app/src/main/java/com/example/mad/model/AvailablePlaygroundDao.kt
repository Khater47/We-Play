package com.example.mad.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao

interface AvailablePlaygroundDao {

    @Query("SELECT * FROM available_playgrounds ORDER BY id ASC")
    fun getAll(): LiveData<List<AvailablePlayground>>

    @Query("SELECT * FROM available_playgrounds ORDER BY date,startTime")
    fun getAvailablePlaygroundOrdered(): LiveData<List<AvailablePlayground>>

    @Query("SELECT * FROM available_playgrounds WHERE date= :date ORDER BY date,startTime")
    fun getAvailablePlaygroundByDate(
        date: String
    ): LiveData<List<AvailablePlayground>>

    @Query("SELECT * FROM available_playgrounds WHERE startTime= :startTime ORDER BY date,startTime")
    fun getAvailablePlaygroundByTime(
        startTime: String,
    ): LiveData<List<AvailablePlayground>>

    @Query("SELECT * FROM available_playgrounds WHERE startTime= :startTime AND date= :date ORDER BY date,startTime")
    fun getAvailablePlaygroundsByAllFilter(
        startTime: String,
        date: String,
    ): LiveData<List<AvailablePlayground>>

//    @Query("SELECT * FROM available_playgrounds WHERE startTime= :startTime AND endTime= :endTime AND date= :date AND playground= :playground AND location= :location")
//    suspend fun getSpecificAvailablePlaygrounds(
//        startTime: String,
//        endTime: String,
//        date: String,
//        playground: String,
//        location: String
//    ): AvailablePlayground?
//

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvailablePlayground(availablePlayground: AvailablePlayground)

    @Delete
    suspend fun deleteAvailablePlayground(availablePlayground: AvailablePlayground)

}