package com.example.mad.model

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface ProfileRatingDao {
    

    @Query("SELECT * FROM profileRating WHERE idProfile=:idProfile ORDER BY rating")
    fun getProfileRatingByIdProfile(idProfile:Int) : LiveData<ProfileRating>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileRating(profileRating: ProfileRating)

    @Delete
    suspend fun deleteProfileRating(profileRating: ProfileRating)

}