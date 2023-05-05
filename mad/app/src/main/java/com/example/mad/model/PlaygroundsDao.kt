package com.example.mad.model


import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PlaygroundsDao {

    @Query("SELECT * FROM playgrounds ORDER BY id ASC")
    fun getAll() : LiveData<List<Playgrounds>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayground(playground: Playgrounds)

    @Delete
    suspend fun deletePlaygrounds(playground: Playgrounds)

}