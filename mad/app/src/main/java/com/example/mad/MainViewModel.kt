package com.example.mad

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad.model.Playground
import com.example.mad.model.PlaygroundRating
import com.example.mad.model.Profile
import com.example.mad.model.Reservation
import com.example.mad.model.TimeSlot
import com.example.mad.model.UserReservation
import com.example.mad.model.UserSport
import com.example.mad.model.toReservation
import com.example.mad.model.toUserReservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

const val PLAYGROUNDS = "playgrounds"
const val PLAYGROUND_RATING = "rating"
const val USERS = "users"
const val USER_SPORT = "sport"
const val RESERVATION = "reservation"

class MainViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val auth: FirebaseAuth = Firebase.auth
    val currentUser = MutableStateFlow(auth.currentUser)

    private val _reservations = MutableLiveData<List<Reservation?>>()
    val reservations: LiveData<List<Reservation?>> = _reservations

//    private val _userReservations = MutableLiveData<List<UserReservation?>>()
//    val userReservations:LiveData<List<UserReservation?>> = _userReservations

    fun onSignInClick(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                currentUser.value = task.result.user
            } else {
                // If sign in fails, display a message to the user.
                currentUser.value = null
            }

        }
    }

    fun onSignOutInClick() = auth.signOut()


    //---------------------
    //Function User Profile
    //---------------------
    fun insertUserProfile(userId: String, profile: Profile) =
        CoroutineScope(Dispatchers.IO).launch {

            db.collection(USERS)
                .document(userId)
                .set(profile)
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
        }



    //---------------------
    //Function User Reservation
    //---------------------

    fun getAllUserReservationByDate(
        userId: String,
        date: String
    ): LiveData<List<UserReservation?>> {

        val resultLiveData = MutableLiveData<List<UserReservation?>>()

        db.collection(USERS)
            .document(userId)
            .collection(RESERVATION)
            .whereEqualTo("date", date)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val reservations =
                        value?.documents?.map {
                            it.toUserReservation()
                        } ?: emptyList()

                    resultLiveData.postValue(reservations)
                } else {
                    Log.d("TAG", "ERROR")
                }
            }
        return resultLiveData
    }

    fun getAllUserReservationDatesInMonth(userId: String, month: String): LiveData<Set<String?>> {

        val resultLiveData = MutableLiveData<Set<String?>>()

        db.collection(USERS)
            .document(userId)
            .collection(RESERVATION)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val dates = mutableListOf<String>()

                    value?.documents?.forEach {

                        val date = it.get("date") as String
                        if (date.contains(month)){
                            dates.add(date)
                        }
                    }

                    resultLiveData.postValue(dates.toSet())
                } else {
                    Log.d("TAG", "ERROR")
                }
            }
        return resultLiveData
    }

    fun getAllUserReservationDates(userId: String): LiveData<Set<String?>> {

        val resultLiveData = MutableLiveData<Set<String?>>()

        db.collection(USERS)
            .document(userId)
            .collection(RESERVATION)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val dates =

                    value?.documents?.map {

                        it.get("date") as String

                    }?: emptySet<String>()

                    resultLiveData.postValue(dates.toSet())
                } else {
                    Log.d("TAG", "ERROR")
                }
            }
        return resultLiveData
    }

    fun insertUserReservation(userId: String, reservationId: String, reservation: UserReservation) =
        CoroutineScope(Dispatchers.IO).launch {

            db.collection(USERS)
                .document(userId)
                .collection(RESERVATION)
                .document(reservationId)
                .set(reservation)
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
        }

    fun updateUserReservation(
        userId: String,
        reservationId: String,
        equipment: Boolean,
        startTime: String,
        endTime: String
    ) =
        CoroutineScope(Dispatchers.IO).launch {

            val data = hashMapOf<String, Any>(
                "equipment" to equipment,
                "startTime" to startTime,
                "endTime" to endTime
            )

            db.collection(USERS)
                .document(userId)
                .collection(RESERVATION)
                .document(reservationId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
        }

    fun deleteUserReservation(userId: String, reservationId: String) =
        CoroutineScope(Dispatchers.IO).launch {
            db.collection(USERS)
                .document(userId)
                .collection(RESERVATION)
                .document(reservationId)
                .delete()
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
        }

}



