package com.example.mad

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mad.model.Playground
import com.example.mad.model.PlaygroundRating
import com.example.mad.model.Reservation
import com.example.mad.model.TimeSlot
import com.example.mad.model.UserReservation
import com.example.mad.model.UserSport


class MainViewModel : ViewModel() {

    private val repo = MainRepository()

    val currentUser = repo.currentUser

    fun onSignInClick(email:String,password:String) {
        repo.onSignInClick(email,password)
    }
    fun onSignOutInClick() =  repo.onSignOutInClick()


    //---------------------
    //Function Playground
    //---------------------
    fun getAllPlaygroundLiveData():LiveData<List<Playground?>> {
        return repo.getAllPlaygroundLiveData()
    }
    fun getPlayground(documentPath:String):LiveData<Playground?>{
        return repo.getPlayground(documentPath)
    }

    fun getPlaygroundBySport(sport:String):LiveData<List<Playground?>>{
        return repo.getPlaygroundBySport(sport)
    }

    fun getPlaygroundByCity(city:String):LiveData<List<Playground?>>{
     return repo.getPlaygroundByCity(city)

    }

    fun getPlaygroundBySportAndCity(sport:String,city:String):LiveData<List<Playground?>>{
        return repo.getPlaygroundBySportAndCity(sport, city)
    }

    fun getRatingPlayground(documentPath: String):LiveData<PlaygroundRating>{
        return repo.getPlaygroundRating(documentPath)
    }

    //---------------------
    //Function Reservation
    //---------------------
    fun insertReservation(reservation: Reservation){
        repo.insertReservation(reservation)
    }
    fun deleteReservation(documentPath: String){
        repo.deleteReservation(documentPath)
    }



    //---------------------
    //Function User (sport,rating_playground)
    //---------------------

    //documentPath = Sport
    fun insertUserSport(documentPath: String,uid:String,userSport: UserSport){
        return repo.insertUserSport(documentPath,uid,userSport)
    }

    fun insertUserReservation(uid:String,userReservation: UserReservation){
        return repo.insertUserReservation(uid,userReservation)
    }

    //---------------------
    //Function TimeSlot
    //---------------------
    fun getTimeSlot():List<TimeSlot>{
        return repo.getTimeSlot()
    }

}



