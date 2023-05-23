package com.example.mad

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mad.model.Playground
import com.example.mad.model.PlaygroundRating
import com.example.mad.model.Reservation
import com.example.mad.model.TimeSlot
import com.example.mad.model.UserReservation
import com.example.mad.model.UserSport
import com.example.mad.model.toPlayground
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow

const val PLAYGROUNDS = "playgrounds"
const val PLAYGROUND_RATING = "rating"
const val USERS = "users"
const val USER_SPORT = "sport"
const val RESERVATION = "reservation"


//documentPath=="" -> exception | documentPath!="" success
//insert = add + update

class MainRepository {

    private val db = FirebaseFirestore.getInstance()

    private  val auth: FirebaseAuth = Firebase.auth
    val currentUser = MutableStateFlow(auth.currentUser)

    fun onSignInClick(email:String,password:String){
//
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                currentUser.value=task.result.user
            } else {
                // If sign in fails, display a message to the user.
                currentUser.value=null
            }

        }
    }


//    .addOnSuccessListener {
//        Log.d("TAG_REPO","$email $password")
//
//        currentUser.value=it.user
//    }
//    .addOnFailureListener {
//        Log.d("TAG","FAILURE")
//
//        currentUser.value=null
//    }

    fun onSignOutInClick() = auth.signOut()



    //---------------------
    //Function Playgrounds
    //---------------------
    fun getAllPlaygroundLiveData(): LiveData<List<Playground?>> {

        val resultLiveData = MutableLiveData<List<Playground?>>()

        db.collection(PLAYGROUNDS)
            .addSnapshotListener { value, error ->
                if(error==null){
                    val playgrounds =
                        value?.documents?.map {
                            it.toPlayground()
                        } ?: emptyList()
                    resultLiveData.value = playgrounds
                }
                else {
                    Log.d("TAG","ERROR")
                }
            }
        return resultLiveData
    }

    fun getPlayground(documentPath:String):LiveData<Playground?>{
        val resultLiveData = MutableLiveData<Playground?>()

        db.collection(PLAYGROUNDS)
            .document(documentPath)
            .get()
            .addOnSuccessListener { result ->
                val playgrounds = result.toPlayground()
                resultLiveData.value = playgrounds
            }
            .addOnFailureListener{
                    exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
        return resultLiveData
    }

    fun getPlaygroundBySport(sport:String):LiveData<List<Playground?>>{
        val resultLiveData = MutableLiveData<List<Playground?>>()

        db.collection(PLAYGROUNDS)
            .whereEqualTo("sport",sport)
            .get()
            .addOnSuccessListener { result ->
                val playgrounds =
                result.documents.map {
                    it.toPlayground()
                }
                resultLiveData.value = playgrounds
            }
            .addOnFailureListener{
                    exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

        return resultLiveData
    }

    fun getPlaygroundByCity(city:String):LiveData<List<Playground?>>{
        val resultLiveData = MutableLiveData<List<Playground?>>()

        db.collection(PLAYGROUNDS)
            .whereEqualTo("city",city)
            .get()
            .addOnSuccessListener { result ->
                val playgrounds =
                    result.documents.map {
                        it.toPlayground()
                    }
                resultLiveData.value = playgrounds
            }
            .addOnFailureListener{
                    exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

        return resultLiveData
    }

    fun getPlaygroundBySportAndCity(sport:String,city:String):LiveData<List<Playground?>>{
        val resultLiveData = MutableLiveData<List<Playground?>>()

        db.collection(PLAYGROUNDS)
            .whereEqualTo("city",city)
            .whereEqualTo("sport",sport)
            .get()
            .addOnSuccessListener { result ->
                val playgrounds =
                    result.documents.map {
                        it.toPlayground()
                    }
                resultLiveData.value = playgrounds
            }
            .addOnFailureListener{
                    exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

        return resultLiveData
    }

    fun getPlaygroundRating(documentPath: String):LiveData<PlaygroundRating>{
        val resultLiveData = MutableLiveData<PlaygroundRating>()

        db.collection(PLAYGROUNDS)
            .document(documentPath)
            .collection(PLAYGROUND_RATING)
            .get()
            .addOnSuccessListener { result ->

                val comments:MutableMap<String,String> = mutableMapOf()
                var sumQuality = 0f
                var sumFacilities = 0f
                var ratings = 0
                result.documents.forEach {

                    val comment = it.get("comment") as String
                    val nickname = it.get("nickname") as String

                    comments[nickname] = comment

                    sumQuality = it.get("quality") as Float
                    sumFacilities = it.get("quality") as Float
                    ratings+=1

                    //comment, quality, facilities, nickname
                    }
                val pr = PlaygroundRating(sumQuality/ratings,sumFacilities/ratings,comments)
                resultLiveData.value = pr
            }
            .addOnFailureListener{
                    exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }


        return resultLiveData

    }



    //---------------------
    //Function User
    //---------------------
    fun insertUserSport(documentPath:String,uid:String,userSport:UserSport){

        db.collection(USERS)
            .document(uid)
            .collection(USER_SPORT)
            .document(documentPath)
            .set(userSport)
            .addOnSuccessListener {
                Log.d("TAG","SUCCESS")
            }
            .addOnFailureListener {
                Log.d("TAG","ERROR")

            }
    }

    fun insertUserReservation(uid:String,userReservation: UserReservation){

        db.collection(USERS)
            .document(uid)
            .collection(RESERVATION)
            .document()
            .set(userReservation)
            .addOnSuccessListener {
                Log.d("TAG","SUCCESS")
            }
            .addOnFailureListener {
                Log.d("TAG","ERROR")

            }
    }


    //---------------------
    //Function Reservation
    //---------------------
    fun insertReservation(reservation: Reservation){
        db.collection(RESERVATION)
            .document()
            .set(reservation)
            .addOnSuccessListener {
                Log.d("TAG","SUCCESS")
            }
            .addOnFailureListener {
                Log.d("TAG","ERROR")

            }
    }

    fun deleteReservation(documentPath:String){
        db.collection(RESERVATION)
            .document(documentPath)
            .delete()
            .addOnSuccessListener {
                Log.d("TAG","SUCCESS")
            }
            .addOnFailureListener {
                Log.d("TAG","ERROR")

            }
    }


    //---------------------
    //Function TimeSlot
    //---------------------
    fun getTimeSlot(): List<TimeSlot> {

        val timeSlotList = mutableListOf<TimeSlot>()

        db.collection(PLAYGROUNDS)
            .addSnapshotListener { value, error ->
                if(error==null){
                    val timeSlot =
                        value?.documents?.map {
                            TimeSlot(
                                it.get("startTime") as String,
                                it.get("endTime") as String
                            )
                        } ?: emptyList()
                    timeSlotList.addAll(timeSlot)
                }
                else {
                    Log.d("TAG","ERROR")
                }
            }
        return timeSlotList
    }


}