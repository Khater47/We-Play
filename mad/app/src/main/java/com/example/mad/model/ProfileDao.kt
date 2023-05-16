package com.example.mad.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile ORDER BY id ASC")
    fun getAll() : LiveData<List<Profile>>

    @Query("SELECT * FROM profile WHERE id=:id")
    fun getProfileById(id:Int) : LiveData<Profile>

    @Query("SELECT * FROM profile WHERE email=:email")
    fun getProfileByEmail(email:String) : LiveData<Profile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    @Delete
    suspend fun deleteProfile(profile: Profile)

}