package com.example.mad.model


import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PlaygroundsDao {

    @Query("SELECT * FROM playgrounds ORDER BY id ASC")
    fun getAll() : LiveData<List<Playgrounds>>

    @Query("SELECT * FROM playgrounds WHERE id=:id")
    fun getPlaygroundById(id:Int) : LiveData<Playgrounds>

    @Query("SELECT DISTINCT sport FROM playgrounds")
    fun getAllSport() : LiveData<List<String>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayground(playground: Playgrounds)

    @Delete
    suspend fun deletePlaygrounds(playground: Playgrounds)

}