package com.example.mad.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import com.example.mad.model.Reservation


@Dao
interface ReservationDao {

    @Query("SELECT * FROM reservations ORDER BY id ASC")
    fun getAll() : LiveData<List<Reservation>>

    @Query("SELECT date FROM reservations")
    fun getAllDate() : LiveData<List<String>>

    @Insert
    suspend fun addReservation(reservation: Reservation)

    @Insert
    suspend fun addReservations(reservation: List<Reservation>)

    @Query("SELECT * FROM reservations WHERE date = :dateText")
    fun getReservationByDate(dateText:String) : LiveData<List<Reservation>>


    @Update
    suspend fun updateReservation(reservation: Reservation)

    @Delete
    suspend fun deleteReservation(reservation: Reservation)

}