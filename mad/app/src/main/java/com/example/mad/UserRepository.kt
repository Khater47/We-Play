package com.example.mad
import androidx.lifecycle.LiveData
import com.example.mad.model.*
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val reservationDao: ReservationDao,
    private val sportDao: SportDao,
    private val playgroundsDao: PlaygroundsDao,
) {

    fun reservations():LiveData<List<Reservation>> = reservationDao.getAll()
    fun reservationsDate():LiveData<List<String>> = reservationDao.getAllDate()

    fun sports():LiveData<List<Sport>> = sportDao.getAll()

    fun playgrounds():LiveData<List<Playgrounds>> = playgroundsDao.getAll()


    suspend fun addReservation(reservation: Reservation){
        reservationDao.addReservation(reservation)
    }

    suspend fun addSport(sport: Sport){
        sportDao.addSport(sport)
    }

    suspend fun addPlayground(playgrounds: Playgrounds){
        playgroundsDao.addPlayground(playgrounds)
    }

    suspend fun addReservations(reservation: List<Reservation>){
        reservationDao.addReservations(reservation)
    }

    suspend fun addSports(sports: List<Sport>){
        sportDao.addSports(sports)
    }

    suspend fun addPlaygrounds(playgrounds: List<Playgrounds>){
        playgroundsDao.addPlaygrounds(playgrounds)
    }

    fun getReservationByDate(dateText:String) :LiveData<List<Reservation>>{
        return reservationDao.getReservationByDate(dateText)
    }


    suspend fun updateReservation(reservation: Reservation){
        reservationDao.updateReservation(reservation)
    }

    suspend fun updateSport(sport: Sport){
        sportDao.updateSport(sport)
    }

    suspend fun updatePlayground(playgrounds: Playgrounds){
        playgroundsDao.updatePlaygrounds(playgrounds)
    }


    suspend fun deleteReservation(reservation: Reservation){
        reservationDao.deleteReservation(reservation)
    }

    suspend fun deleteSport(sport: Sport){
        sportDao.deleteSport(sport)
    }

    suspend fun deletePlayground(playgrounds: Playgrounds){
        playgroundsDao.deletePlaygrounds(playgrounds)
    }


}
