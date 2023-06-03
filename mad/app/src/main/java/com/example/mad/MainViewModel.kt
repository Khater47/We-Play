package com.example.mad

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad.common.getTimeSlot
import com.example.mad.common.getToday
import com.example.mad.model.Comment
import com.example.mad.model.Friend
import com.example.mad.model.Invitation
import com.example.mad.model.Playground
import com.example.mad.model.PlaygroundRating
import com.example.mad.model.Profile
import com.example.mad.model.ProfileRating
import com.example.mad.model.ProfileSport
import com.example.mad.model.Reservation
import com.example.mad.model.Stat
import com.example.mad.model.UserReservation
import com.example.mad.model.toComment
import com.example.mad.model.toFriend
import com.example.mad.model.toInvitation
import com.example.mad.model.toPlayground
import com.example.mad.model.toProfile
import com.example.mad.model.toProfileSport
import com.example.mad.model.toReservation
import com.example.mad.model.toStat
import com.example.mad.model.toUserReservation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

const val PLAYGROUNDS = "playgrounds"
const val RATING = "rating"
const val USERS = "users"
const val SPORT = "sport"
const val RESERVATION = "reservation"
const val INVITATION = "invitation"
const val DELAY = 1000L


class MainViewModel : ViewModel() {

    private val repo = MainRepository()

    val currentUser = repo.currentUser

    //splashScreen
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val loadingProgressBar = mutableStateOf(false)

    private val _profile = MutableLiveData<Profile?>()
    val profile: LiveData<Profile?> = _profile


    private val _playground = MutableLiveData<Playground?>()
    val playground: LiveData<Playground?> = _playground

    private val _comments = MutableLiveData<List<Comment?>>()
    val comments: LiveData<List<Comment?>> = _comments

    private val _availableTimeSlot = MutableLiveData<Set<String>>()
    val availableTimeSlot = _availableTimeSlot

    private val _playgrounds = MutableLiveData<List<Playground?>>()
    val playgrounds: LiveData<List<Playground?>> = _playgrounds

    private val _userSports = MutableLiveData<List<ProfileSport>>()
    val userSports: LiveData<List<ProfileSport>> = _userSports

    //list of user reservation
    private val _userReservation = MutableLiveData<List<UserReservation>>()
    val userReservation: LiveData<List<UserReservation>> = _userReservation

//    private val _profileRating = MutableLiveData<List<ProfileRating>>()
//    val profileRating: LiveData<List<ProfileRating>> = _profileRating

    private val _dates = MutableLiveData<List<String>>()
    val dates: LiveData<List<String>> = _dates

    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friends

    private val _statInvitation = MutableLiveData<Stat?>()
    val statInvitation: LiveData<Stat?> = _statInvitation


    //one user reservation
    private val _reservation = MutableLiveData<Reservation>()
    val reservation: LiveData<Reservation> = _reservation

    private val _invitation = MutableLiveData<List<Invitation>>()
    val invitation: LiveData<List<Invitation>> = _invitation

    //splashScreen
    init {
        viewModelScope.launch {
            delay(DELAY)
            _isLoading.value = false
        }
    }

    fun deleteInvitation(timestamp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteInvitation(timestamp)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun getFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val f = repo.getFriends()?.documents
                    ?.mapNotNull {
                        it.toFriend()
                    } ?: emptyList()

                _friends.postValue(f)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun getStatBySport(sport: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val stat = repo.getStatBySport(sport)?.toStat()
                _statInvitation.postValue(stat)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun getInvitations() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingProgressBar.value = true
                delay(DELAY)
                val i = repo.getInvitations()?.documents?.mapNotNull {
                    it.toInvitation()
                } ?: emptyList()

                val filterInvitations = mutableListOf<Invitation>()

                if(i.isNotEmpty()){
                    val todayString = getToday()
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val today = dateFormat.parse(todayString)
                    if (today != null) {

                        i.forEach {
                            val d = dateFormat.parse(it.date)
                            if(d!=null && !d.before(today)){
                                filterInvitations.add(it)
                            }
                        }
                    }
                }

                _invitation.postValue(filterInvitations)
                loadingProgressBar.value = false

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun acceptInvitation(timestamp: String, notification: Invitation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.acceptInvitation(timestamp, notification)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun registration(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.registration(email, password)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun logIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingProgressBar.value = true
            try {
                val loggedUser = repo.logIn(email, password).user
                repo.currentUser.value = loggedUser

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "Error")
            }
            delay(DELAY)
            loadingProgressBar.value = false

        }
    }

    fun logOut() = viewModelScope.launch(Dispatchers.IO) {
        loadingProgressBar.value = true
        delay(DELAY)
        repo.logout()
        loadingProgressBar.value = false
    }


    //PROFILE SPORT GET, ADD, DELETE
    fun insertUserSport(profileSport: ProfileSport) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertUserSport(profileSport)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun deleteUserSport(sport: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteUserSport(sport)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun getUserSport() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingProgressBar.value = true
            delay(DELAY)

            try {
                val sport =
                    repo.getUserSport()?.documents?.mapNotNull {
                        it.toProfileSport()
                    } ?: emptyList()

                _userSports.postValue(sport)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "Error")
            }

            loadingProgressBar.value = false
        }
    }


    //PLAYGROUND (GET ALL, SPORT, CITY, SPORT+CITY, ID)

    fun getPlaygrounds(sport: String = "", city: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                loadingProgressBar.value = true

                val playgroundList =
                    if (sport.isEmpty() && city.isEmpty()) {
                        repo.getPlaygrounds().documents.mapNotNull {
                            it.toPlayground()
                        }

                    } else if (sport.isNotBlank() && city.isEmpty()) {
                        repo.getPlaygroundsBySport(sport).documents.mapNotNull {
                            it.toPlayground()
                        }
                    } else if (sport.isEmpty() && city.isNotBlank()) {
                        repo.getPlaygroundsByCity(city).documents.mapNotNull {
                            it.toPlayground()
                        }
                    } else {
                        repo.getPlaygroundsByCityAndSport(city, sport).documents.mapNotNull {
                            it.toPlayground()
                        }
                    }
                delay(DELAY)
                _playgrounds.postValue(playgroundList)
                loadingProgressBar.value = false

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }

        }
    }

    fun getPlaygroundById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val p = repo.getPlaygroundById(id).toPlayground()
                _playground.postValue(p)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun getPlaygroundComments(
        playgroundId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val c = repo.getPlaygroundComments(playgroundId).mapNotNull { it.toComment() }
                _comments.postValue(c)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }


    //PROFILE, PROFILE EDIT
    fun insertUserProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertUserProfile(profile)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun insertUserRegistrationProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertUserRegistrationProfile(profile)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val p = repo.getUserProfile()?.toProfile()
                _profile.postValue(p)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

//    fun getRatingsPlayground(id: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val r = repo.getRatingsPlayground(id).documents.mapNotNull {
//                    it.toPlaygroundRating()
//                }
//                //_ratingPlayground.postValue(r)
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.d("TAG", e.localizedMessage ?: "")
//            }
//        }
//    }


    fun insertUserRating(rating: ProfileRating) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertUserRating(rating)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun insertUserPlaygroundRating(id: String, playgroundRating: PlaygroundRating) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertUserPlaygroundRating(id, playgroundRating)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }

    }


    //---------------------

    fun sendInvitation(invitation: Invitation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.sendInvitation(invitation)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }

    }

    fun getAllUserReservationByDate(
        date: String
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val r = repo.getAllUserReservationByDate(date)?.documents?.mapNotNull {
                    it.toUserReservation()
                } ?: emptyList()
                _userReservation.postValue(r)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun getUserToRatedPlayground() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingProgressBar.value = true
                delay(DELAY)

                val list = repo.getUserToRatedPlayground()

                _userReservation.postValue(list)
                loadingProgressBar.value = false

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }


    fun insertUserReservation(reservationId: String, reservation: UserReservation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertUserReservation(reservationId, reservation)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }


    }

    fun getDatesReservationByMonth(month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val d = repo.getAllUserReservationDates()?.documents
                    ?.map { it.get("date").toString() }
                    ?.filter {
                        it.contains(month)
                    } ?: emptyList()

                _dates.postValue(d)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun updateUserReservation(
        reservationId: String,
        data: HashMap<String, Any>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.updateUserReservation(reservationId, data)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }

    }

    fun deleteUserReservation(reservationId: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteUserReservation(reservationId)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }


    }

    fun insertReservation(reservationId: String, reservation: Reservation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertReservation(reservationId, reservation)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }

    }

    fun getReservationById(reservationId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repo.getReservationById(reservationId)
                    ?.toReservation()

                if (result != null) {
                    val r = Reservation(
                        address = result.address,
                        city = result.city,
                        date = result.date,
                        email = result.email,
                        endTime = result.endTime,
                        equipment = result.equipment,
                        id = result.id,
                        playground = result.playground,
                        sport = result.sport,
                        startTime = result.startTime,
                    )
                    _reservation.postValue(r)
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }

    fun deleteReservation(reservationId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteReservation(reservationId)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }

    //
    fun updateReservation(
        reservationId: String,
        data: HashMap<String, Any>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.updateReservation(reservationId, data)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }
    }


    fun getTimeSlotReservationByPlaygroundAndDate(date: String, address: String, city: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val timeSlot = repo.getTimeSlotReservationByPlaygroundAndDate(date, address, city)
                    .documents.filter {
                        it.get("date").toString() == date &&
                                it.get("address").toString() == address &&
                                it.get("city").toString() == city
                    }.map {
                        it.get("startTime").toString() + "-" + it.get("endTime").toString()
                    }.toSet()

                //compute time slot available
                val allTimeSlot = getTimeSlot().toMutableStateList()
                allTimeSlot.removeAll {
                    it in timeSlot.toList()
                }

                _availableTimeSlot.postValue(allTimeSlot.toSet())


            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.localizedMessage ?: "")
            }
        }

    }

}





