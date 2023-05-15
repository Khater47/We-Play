package com.example.mad

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad.model.Playgrounds
import com.example.mad.model.Profile
import com.example.mad.model.ProfileRating
import com.example.mad.model.ProfileSport
import com.example.mad.model.Reservation
import com.example.mad.model.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repo: UserRepository,
) : ViewModel() {


    //--------------------------------------------
    //Reservation functions
    //--------------------------------------------

    val reservations: LiveData<List<Reservation>> = repo.reservations()
    val reservationsDate: LiveData<List<String>> = repo.reservationsDate()
    fun getReservationByDate(dateText: String): LiveData<List<Reservation>> {
        return repo.getReservationByDate(dateText)
    }

    fun insertReservation(reservation: Reservation) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertReservation(reservation)
        }
    }

    fun deleteReservation(reservation: Reservation) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteReservation(reservation)
        }
    }


    //--------------------------------------------
    //Playground functions
    //--------------------------------------------
    val playgrounds: LiveData<List<Playgrounds>> = repo.playgrounds()
    val sports: LiveData<List<String>> = repo.getAllSport()

    //--------------------------------------------
    //Profile functions
    //--------------------------------------------
    fun getProfileByEmail(email: String): LiveData<Profile> = repo.getProfileByEmail(email)

    fun getProfileById(id: Int): LiveData<Profile> = repo.getProfileById(id)

    fun insertProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertProfile(profile)
        }
    }

    fun deleteProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteProfile(profile)
        }
    }

    //--------------------------------------------
    //Profile Sport functions
    //--------------------------------------------
    fun getProfileSportByIdProfile(idProfile: Int): LiveData<List<ProfileSport>> =
        repo.getProfileSportByIdProfile(idProfile)

    fun insertProfileSport(profileSport: ProfileSport) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertProfileSport(profileSport)
        }
    }

    fun updateProfileSport(profileSport: ProfileSport) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateProfileSport(profileSport)
        }
    }

    fun deleteProfileSport(profileSport: ProfileSport) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteProfileSport(profileSport)
        }
    }


    //--------------------------------------------
    //Profile Rating functions
    //--------------------------------------------
    fun getProfileRatingByIdProfile(idProfile: Int): LiveData<List<ProfileRating>> =
        repo.getProfileRatingByIdProfile(idProfile)

    fun insertProfileRating(profileRating: ProfileRating) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertProfileRating(profileRating)
        }
    }

    fun deleteProfileRating(profileRating: ProfileRating) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteProfileRating(profileRating)
        }
    }


    //--------------------------------------------
    //Time Slot functions
    //--------------------------------------------

    fun getTimeSlotByTime(time: String): LiveData<TimeSlot> = repo.getTimeSlotByTime(time)

}