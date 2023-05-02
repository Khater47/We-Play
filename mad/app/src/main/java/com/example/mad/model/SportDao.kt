package com.example.mad.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SportDao {

    @Query("SELECT * FROM sports ORDER BY id ASC")
    fun getAll() : LiveData<List<Sport>>

    @Insert
    suspend fun addSport(sport: Sport)

    @Insert
    suspend fun addSports(sports: List<Sport>)

    @Update
    suspend fun updateSport(sport: Sport)

    @Delete
    suspend fun deleteSport(sport: Sport)

}