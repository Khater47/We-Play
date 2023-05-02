package com.example.mad

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad.model.Playgrounds
import com.example.mad.model.Reservation
import com.example.mad.model.Sport
import com.example.mad.model.UserDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repo:UserRepository): ViewModel(){

    val reservations:LiveData<List<Reservation>> = repo.reservations()
    val reservationsDate:LiveData<List<String>> = repo.reservationsDate()
    val sports:LiveData<List<Sport>> = repo.sports()
    val playgrounds:LiveData<List<Playgrounds>> = repo.playgrounds()

    fun addReservation(reservation: Reservation){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addReservation(reservation)
        }
    }
    fun addPlayground(playgrounds: Playgrounds){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addPlayground(playgrounds)
        }
    }
    fun addSport(sport: Sport){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addSport(sport)
        }
    }


    fun addReservations(reservation: List<Reservation>){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addReservations(reservation)
        }
    }
    fun addPlaygrounds(playgrounds: List<Playgrounds>){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addPlaygrounds(playgrounds)
        }
    }

    fun addSports(sports: List<Sport>){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addSports(sports)
        }
    }

    fun getReservationByDate(dateText:String):LiveData<List<Reservation>> {
             return repo.getReservationByDate(dateText)
    }

    fun updateReservation(reservation: Reservation){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateReservation(reservation)
        }
    }

    fun updatePlayground(playgrounds: Playgrounds){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updatePlayground(playgrounds)
        }
    }

    fun updateSport(sport: Sport){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateSport(sport)
        }
    }


    fun deleteReservation(reservation: Reservation){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteReservation(reservation)
        }
    }

    fun deletePlayground(playgrounds: Playgrounds){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deletePlayground(playgrounds)
        }
    }

    fun deleteSport(sport: Sport){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteSport(sport)
        }
    }
}