package com.example.mad.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy


@Dao
interface ReservationDao {

    @Query("SELECT * FROM reservations ORDER BY id ASC")
    fun getAll() : LiveData<List<Reservation>>

    @Query("SELECT date FROM reservations")
    fun getAllDate() : LiveData<List<String>>


    @Query("SELECT * FROM reservations WHERE id=:id")
    fun getReservationById(id:Int) : LiveData<Reservation>

    @Query("SELECT * FROM reservations WHERE date = :date")
    fun getReservationByDate(date:String) : LiveData<List<Reservation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation)

    @Delete
    suspend fun deleteReservation(reservation: Reservation)

}