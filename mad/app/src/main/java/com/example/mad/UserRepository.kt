package com.example.mad
import androidx.lifecycle.LiveData
import com.example.mad.model.*
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val reservationDao: ReservationDao,
    private val playgroundsDao: PlaygroundsDao,
    private val profileDao: ProfileDao,
    private val profileSportDao: ProfileSportDao,
    private val profileRatingDao: ProfileRatingDao,
    private val timeSlotDao: TimeSlotDao
) {

    //--------------------------------------------
    //Reservation functions
    //--------------------------------------------

    fun reservations():LiveData<List<Reservation>> = reservationDao.getAll()
    fun reservationsDate():LiveData<List<String>> = reservationDao.getAllDate()
    fun getReservationByDate(date:String) :LiveData<List<Reservation>> = reservationDao.getReservationByDate(date)

    suspend fun insertReservation(reservation: Reservation){
        reservationDao.insertReservation(reservation)
    }

    suspend fun deleteReservation(reservation: Reservation){
        reservationDao.deleteReservation(reservation)
    }


    //--------------------------------------------
    //Playground functions
    //--------------------------------------------
    fun playgrounds():LiveData<List<Playgrounds>> = playgroundsDao.getAll()



    //--------------------------------------------
    //Profile functions
    //--------------------------------------------
    fun getProfileByEmail(email:String):LiveData<Profile> = profileDao.getProfileByEmail(email)

    fun getProfileById(id:Int):LiveData<Profile> = profileDao.getProfileById(id)


    suspend fun insertProfile(profile: Profile){
        profileDao.insertProfile(profile)
    }

    suspend fun deleteProfile(profile: Profile){
        profileDao.deleteProfile(profile)
    }


    //--------------------------------------------
    //Profile Sport functions
    //--------------------------------------------

    fun getProfileSportByIdProfile(idProfile:Int):LiveData<ProfileSport> = profileSportDao.getProfileSportByIdProfile(idProfile)

    suspend fun insertProfileSport(profileSport: ProfileSport){
        profileSportDao.insertProfileSport(profileSport)
    }
    suspend fun deleteProfileSport(profileSport: ProfileSport){
        profileSportDao.deleteProfileSport(profileSport)
    }


    //--------------------------------------------
    //Profile Rating functions
    //--------------------------------------------

    fun getProfileRatingByIdProfile(idProfile:Int):LiveData<ProfileRating> = profileRatingDao.getProfileRatingByIdProfile(idProfile)

    suspend fun insertProfileRating(profileRating: ProfileRating){
        profileRatingDao.insertProfileRating(profileRating)
    }

    suspend fun deleteProfileRating(profileRating: ProfileRating){
        profileRatingDao.deleteProfileRating(profileRating)
    }

    //--------------------------------------------
    //Time Slot functions
    //--------------------------------------------
    fun getTimeSlotByTime(time:String):LiveData<TimeSlot> = timeSlotDao.getTimeSlotByTime(time)


}
