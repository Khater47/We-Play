package com.example.mad.model


import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PlaygroundsDao {

    @Query("SELECT * FROM playgrounds ORDER BY id ASC")
    fun getAll() : LiveData<List<Playgrounds>>

    @Insert
    suspend fun addPlayground(playgrounds: Playgrounds)

    @Insert
    suspend fun addPlaygrounds(playgrounds: List<Playgrounds>)

    @Update
    suspend fun updatePlaygrounds(playgrounds: Playgrounds)

    @Delete
    suspend fun deletePlaygrounds(playgrounds: Playgrounds)

}