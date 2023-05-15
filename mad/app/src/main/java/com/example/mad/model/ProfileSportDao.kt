package com.example.mad.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileSportDao {


    @Query("SELECT * FROM profileSport WHERE idProfile=:idProfile")
    fun getProfileSportByIdProfile(idProfile: Int): LiveData<List<ProfileSport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileSport(profileSport: ProfileSport)

    @Update
    suspend fun updateProfileSport(profileSport: ProfileSport)

    @Delete
    suspend fun deleteProfileSport(profileSport: ProfileSport)

}