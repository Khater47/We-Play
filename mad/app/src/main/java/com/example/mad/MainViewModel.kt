package com.example.mad

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad.model.Playground
import com.example.mad.model.PlaygroundRating
import com.example.mad.model.Profile
import com.example.mad.model.ProfileSport
import com.example.mad.model.Reservation
import com.example.mad.model.UserReservation
import com.example.mad.model.toPlayground
import com.example.mad.model.toPlaygroundRating
import com.example.mad.model.toProfile
import com.example.mad.model.toProfileSport
import com.example.mad.model.toUserReservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val PLAYGROUNDS = "playgrounds"
const val PLAYGROUND_RATING = "rating"
const val USERS = "users"
const val USER_SPORT = "sport"
const val RESERVATION = "reservation"

class MainViewModel : ViewModel() {

    //splashScreen
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val loadingProgressBar = mutableStateOf(false)

    private val db = FirebaseFirestore.getInstance()

    private val auth: FirebaseAuth = Firebase.auth
    val currentUser = MutableStateFlow(auth.currentUser)



    //splashScreen
    init {
        viewModelScope.launch {
            delay(3000)
            _isLoading.value=false
        }
    }

    fun onSignInClick(email: String, password: String)  {

        loadingProgressBar.value=true

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                currentUser.value = task.result.user

            } else {
                // If sign in fails, display a message to the user.
//                Log.d("TAG_EXCEPTION",task.exception?.localizedMessage?:"Wrong credentials")
                currentUser.value = null
            }

            loadingProgressBar.value=false

        }
    }


    fun onSignOutInClick() {
        loadingProgressBar.value=true
        auth.signOut()
        currentUser.value=null
        loadingProgressBar.value=false
    }


    //---------------------
    //Function User Profile
    //---------------------
    fun insertUserProfile(userId: String, profile: Profile) =
        CoroutineScope(Dispatchers.IO).launch {
            loadingProgressBar.value=true

//            delay(2000)

            db.collection(USERS)
                .document(userId)
                .set(profile)
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
            loadingProgressBar.value=false
        }

    fun getProfileById(id:String): LiveData<Profile?> {

        loadingProgressBar.value=true

        val resultLiveData = MutableLiveData<Profile?>()

        db.collection(USERS)
            .document(id)
            .get()
            .addOnSuccessListener {
                    document -> resultLiveData.postValue(document.toProfile())
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting playground by id ${exception.localizedMessage}")
            }
        loadingProgressBar.value=false

        return resultLiveData
    }




    //---------------------
    //Function User Sport
    //---------------------
    fun insertUserProfileSport(userId: String, profileSport: ProfileSport) =
        CoroutineScope(Dispatchers.IO).launch {
            loadingProgressBar.value=true

            db.collection(USERS)
                .document(userId)
                .collection(USER_SPORT)
                .document(profileSport.sport)
                .set(profileSport)
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
            loadingProgressBar.value=false

        }

    fun deleteUserProfileSport(userId: String, sport: String) =
        CoroutineScope(Dispatchers.IO).launch {
            loadingProgressBar.value=true


            db.collection(USERS)
                .document(userId)
                .collection(USER_SPORT)
                .document(sport)
                .delete()
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
            loadingProgressBar.value=false
        }

    fun getAllUserProfileSport(
        userId: String,
    ): LiveData<List<ProfileSport?>> {


        val resultLiveData = MutableLiveData<List<ProfileSport?>>()

        loadingProgressBar.value=true

        db.collection(USERS)
            .document(userId)
            .collection(USER_SPORT)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val profileSport =
                        value?.documents?.map {
                            it.toProfileSport()
                        } ?: emptyList()


                    resultLiveData.postValue(profileSport)
                } else {
                    Log.d("TAG", "ERROR")
                }
            }
        loadingProgressBar.value=false
        return resultLiveData
    }


    //---------------------
    //Function Rating Playground
    //---------------------
    fun getAllPlaygroundRating(id:String
    ): LiveData<List<PlaygroundRating?>> {

        val resultLiveData = MutableLiveData<List<PlaygroundRating?>>()
        loadingProgressBar.value=true

        db.collection(PLAYGROUNDS)
            .document(id)
            .collection(PLAYGROUND_RATING)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val playgroundRating =
                        value?.documents?.map {
                            it.toPlaygroundRating()
                        } ?: emptyList()

                    resultLiveData.postValue(playgroundRating)
                } else {
                    Log.d("TAG", "ERROR")
                }
            }
        loadingProgressBar.value=false
        return resultLiveData
    }

    fun insertUserPlaygroundRating(id:String,playgroundRating: PlaygroundRating) =
        CoroutineScope(Dispatchers.IO).launch {
            loadingProgressBar.value=true

            db.collection(PLAYGROUNDS)
                .document(id)
                .collection(PLAYGROUND_RATING)
                .document(playgroundRating.nickname)
                .set(playgroundRating)
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
            loadingProgressBar.value=false

        }



    //---------------------
    //Function Playground
    //---------------------
    fun getPlaygrounds(
    ): LiveData<List<Playground?>> {

        val resultLiveData = MutableLiveData<List<Playground?>>()
        loadingProgressBar.value=true

        db.collection(PLAYGROUNDS)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val playground =
                        value?.documents?.map {
                            it.toPlayground()
                        } ?: emptyList()

                    resultLiveData.postValue(playground)
                } else {
                    Log.d("TAG", "ERROR")
                }
            }
        loadingProgressBar.value=false

        return resultLiveData
    }

    fun getPlaygroundsBySport(sport:String
    ): LiveData<List<Playground?>> {

        val resultLiveData = MutableLiveData<List<Playground?>>()
        loadingProgressBar.value=true

        db.collection(PLAYGROUNDS)
            .whereEqualTo("sport",sport)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val playground =
                        value?.documents?.map {
                            it.toPlayground()
                        } ?: emptyList()

                    resultLiveData.postValue(playground)
                } else {
                    Log.d("TAG", "ERROR")
                }
            }
        loadingProgressBar.value=false
        return resultLiveData
    }

    fun getPlaygroundsByLocation(location:String
    ): LiveData<List<Playground?>> {

        val resultLiveData = MutableLiveData<List<Playground?>>()
        loadingProgressBar.value=true

        db.collection(PLAYGROUNDS)
            .whereEqualTo("city",location)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val playground =
                        value?.documents?.map {
                            it.toPlayground()
                        } ?: emptyList()

                    resultLiveData.postValue(playground)
                } else {
                    Log.d("TAG", "ERROR")
                }
            }
        loadingProgressBar.value=false

        return resultLiveData
    }

    fun getPlaygroundsByLocationAndSport(sport:String,location:String
    ): LiveData<List<Playground?>> {

        val resultLiveData = MutableLiveData<List<Playground?>>()
        loadingProgressBar.value=true

        db.collection(PLAYGROUNDS)
            .whereEqualTo("city",location)
            .whereEqualTo("sport",sport)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val playground =
                        value?.documents?.map {
                            it.toPlayground()
                        } ?: emptyList()

                    resultLiveData.postValue(playground)
                } else {
                    Log.d("TAG", "ERROR")
                }
            }
        loadingProgressBar.value=false
        return resultLiveData
    }

    fun getPlaygroundById(id:String): LiveData<Playground?> {

        val resultLiveData = MutableLiveData<Playground?>()
        loadingProgressBar.value=true

        db.collection(PLAYGROUNDS)
            .document(id)
            .get()
            .addOnSuccessListener {
                document -> resultLiveData.postValue(document.toPlayground())
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting playground by id ${exception.localizedMessage}")
            }
        loadingProgressBar.value=false
        return resultLiveData
    }


    //---------------------
    //Function User Reservation
    //---------------------

    fun getAllUserReservationByDate(
        userId: String,
        date: String
    ): LiveData<List<UserReservation?>> {

        val resultLiveData = MutableLiveData<List<UserReservation?>>()
        loadingProgressBar.value=true

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
        loadingProgressBar.value=false
        return resultLiveData
    }

    fun getAllUserPastReservation(
        userId: String,
        today: String
    ): LiveData<List<UserReservation?>> {

        val resultLiveData = MutableLiveData<List<UserReservation?>>()
        loadingProgressBar.value=true

        db.collection(USERS)
            .document(userId)
            .collection(RESERVATION)
            .whereLessThan("date", today)
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
        loadingProgressBar.value=false
        return resultLiveData
    }


    fun getAllUserReservationDatesInMonth(userId: String, month: String): LiveData<Set<String?>> {

        val resultLiveData = MutableLiveData<Set<String?>>()
        loadingProgressBar.value=true

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
        loadingProgressBar.value=false
        return resultLiveData
    }

    fun getAllUserReservationDates(userId: String): LiveData<Set<String?>> {

        val resultLiveData = MutableLiveData<Set<String?>>()
        loadingProgressBar.value=true

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
        loadingProgressBar.value=false

        return resultLiveData
    }

    fun insertUserReservation(userId: String, reservationId: String, reservation: UserReservation) =
        CoroutineScope(Dispatchers.IO).launch {
            loadingProgressBar.value=true

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
            loadingProgressBar.value=false

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
            loadingProgressBar.value=true

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
            loadingProgressBar.value=false

        }

    fun deleteUserReservation(userId: String, reservationId: String) =
        CoroutineScope(Dispatchers.IO).launch {
            loadingProgressBar.value=true

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
            loadingProgressBar.value=false

        }


    //---------------------
    //Function Reservation
    //---------------------
    fun insertReservation(reservationId: String, reservation: Reservation) =
        CoroutineScope(Dispatchers.IO).launch {
            loadingProgressBar.value=true

            db.collection(RESERVATION)
                .document(reservationId)
                .set(reservation)
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
            loadingProgressBar.value=false

        }

    fun deleteReservation(reservationId: String) =
        CoroutineScope(Dispatchers.IO).launch {
            loadingProgressBar.value=true

            db.collection(RESERVATION)
                .document(reservationId)
                .delete()
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
            loadingProgressBar.value=false

        }

    fun updateReservation(
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
            loadingProgressBar.value=true

            db.collection(RESERVATION)
                .document(reservationId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("TAG", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("TAG", "ERROR")

                }
            loadingProgressBar.value=false
        }

}



