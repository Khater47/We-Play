package com.example.mad

import android.util.Log
import com.example.mad.common.getToday
import com.example.mad.model.Invitation
import com.example.mad.model.PlaygroundRating
import com.example.mad.model.Profile
import com.example.mad.model.ProfileRating
import com.example.mad.model.ProfileSport
import com.example.mad.model.Reservation
import com.example.mad.model.UserReservation
import com.example.mad.model.toProfileRating
import com.example.mad.model.toUserReservation
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale


class MainRepository {

    private val auth: FirebaseAuth = Firebase.auth

    private val db = FirebaseFirestore.getInstance()

    val currentUser = MutableStateFlow(auth.currentUser)
    

    //LOGIN & LOGOUT
    suspend fun logIn(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    fun logout() {
        auth.signOut()
        currentUser.value = null
    }


    //PROFILE SPORT (GET, ADD, DELETE)

    suspend fun insertUserSport(profileSport: ProfileSport) {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            db.collection(USERS)
                .document(email)
                .collection(SPORT)
                .document(profileSport.sport)
                .set(profileSport).await()
        }

    }

    suspend fun deleteUserSport(sport: String) {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {

            db.collection(USERS)
                .document(email)
                .collection(SPORT)
                .document(sport)
                .delete()
                .await()
        }
    }

    suspend fun getFriends():QuerySnapshot?{
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            return db.collection(USERS)
                .whereNotEqualTo("email",email)
                .get().await()
        }

        return null
    }

    suspend fun getUserSport(): QuerySnapshot? {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            return db.collection(USERS)
                .document(email)
                .collection(SPORT)
                .get()
                .await()
        }
        return null

    }

    suspend fun sendInvitation(invitation: Invitation) {
        val email = currentUser.value?.email?:""

        if(email.isNotEmpty()){
            db.collection(INVITATION)
                .document(invitation.id+" "+invitation.emailReceiver)
                .set(invitation)
                .await()
        }
    }

    suspend fun getStatBySport(sport:String):DocumentSnapshot?{
        val email = currentUser.value?.email?:""

        if(email.isNotEmpty()){
           return  db.collection(USERS)
                .document(email)
                .collection(SPORT)
                .document(sport)
                .get().await()

        }
        return null
    }

    suspend fun getInvitations(): QuerySnapshot? {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            return db.collection(INVITATION)
                .whereEqualTo("emailReceiver", email)
                .get().await()
        }
        return null
    }

    suspend fun deleteInvitation(timestamp: String) {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            db.collection(INVITATION)
                .document(timestamp)
                .delete().await()
        }
    }

    suspend fun acceptInvitation(timestamp: String, data: Invitation) {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {

            val ur = data.toUserReservation()

            //delete invitation 
            db.collection(INVITATION)
                .document(timestamp)
                .delete().await()

            //add user reservation 
            db.collection(USERS)
                .document(email)
                .collection(RESERVATION)
                .document(timestamp)
                .set(ur).await()

        }
    }
    //PLAYGROUND (GET ALL, SPORT, CITY, SPORT+CITY, ID)

    suspend fun getPlaygrounds(
    ): QuerySnapshot {
        return db.collection(PLAYGROUNDS).get().await()
    }

    suspend fun getPlaygroundsBySport(
        sport: String
    ): QuerySnapshot {
        return db.collection(PLAYGROUNDS).whereEqualTo("sport", sport).get().await()
    }

    suspend fun getPlaygroundsByCity(
        city: String
    ): QuerySnapshot {

        return db.collection(PLAYGROUNDS)
            .whereEqualTo("city", city)
            .get()
            .await()
    }

    suspend fun getPlaygroundsByCityAndSport(
        city: String, sport: String
    ): QuerySnapshot {
        return db.collection(PLAYGROUNDS)
            .whereEqualTo("city", city)
            .whereEqualTo("sport", sport)
            .get().await()
    }

    suspend fun getPlaygroundById(id: String): DocumentSnapshot {

        return db.collection(PLAYGROUNDS)
            .document(id)
            .get()
            .await()
    }

    //PROFILE, PROFILE EDIT

    suspend fun insertUserProfile(profile: Profile) {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {

            db.collection(USERS)
                .document(email)
                .set(profile)
                .await()
        }
    }

    suspend fun insertUserRegistrationProfile(profile: Profile) {

        db.collection(USERS)
            .document(profile.email)
            .set(profile)
            .await()
    }

    suspend fun getUserProfile(): DocumentSnapshot? {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            return db.collection(USERS)
                .document(email)
                .get()
                .await()
        }

        return null
    }


    suspend fun getRatingsPlayground(
        id: String
    ): QuerySnapshot {

        return db.collection(PLAYGROUNDS)
            .document(id)
            .collection(RATING)
            .get().await()

    }


    suspend fun insertUserRating(rating: ProfileRating) {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            db.collection(USERS)
                .document(email)
                .collection(RATING)
                .document(rating.address + " " + rating.city)
                .set(rating)
                .await()
        }

    }


    suspend fun insertUserPlaygroundRating(id: String, playgroundRating: PlaygroundRating) {
        db.collection(PLAYGROUNDS)
            .document(id)
            .collection(RATING)
            .document(playgroundRating.nickname)
            .set(playgroundRating)
            .await()
    }

    suspend fun registration(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun getPlaygroundComments(playgroundId: String): QuerySnapshot {
        return db.collection(PLAYGROUNDS)
            .document(playgroundId)
            .collection(RATING)
            .get().await()
    }

    suspend fun getAllUserReservationByDate(
        date: String
    ): QuerySnapshot? {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            return db.collection(USERS)
                .document(email)
                .collection(RESERVATION)
                .whereEqualTo("date", date).get().await()
        }
        return null
    }

    suspend fun getUserToRatedPlayground(): List<UserReservation> {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            val ratedPlaygrounds =
                getUserRatedPlaygrounds()?.documents?.mapNotNull { it.toProfileRating() }
                    ?: emptyList()

            val  userReservations =  getAllUserReservation()?.documents?.mapNotNull{
                it.toUserReservation()
            }?: emptyList()

            val pastReservation = mutableListOf<UserReservation>()

            if(userReservations.isNotEmpty()){
                val todayString = getToday()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val today = dateFormat.parse(todayString)
                if (today != null) {

                    userReservations.forEach {
                        val d = dateFormat.parse(it.date)
                        if(d!=null && d.before(today)){
                            pastReservation.add(it)
                        }
                    }
                }
            }

//            Log.d("TAG_RATED",ratedPlaygrounds.size.toString())//1
//            Log.d("TAG_PAST_RESERVATION",pastReservation.size.toString())//2

            val newReservations = pastReservation
                .groupBy { it.city + " " + it.address }
                .values.map { it.first() }.toMutableList()

//            Log.d("TAG_GROUP_BY",newReservations.size.toString()) //2

            if (ratedPlaygrounds.isNotEmpty()) {
                ratedPlaygrounds.forEach { Log.d("TAG", it.toString()) }

                newReservations.removeAll {
                    it.address in ratedPlaygrounds.map { p -> p.address } &&
                            it.city in ratedPlaygrounds.map { rp -> rp.city }
                }
            }
//            Log.d("TAG_FINAL",newReservations.size.toString()) //1

            return newReservations.toList()
        }
        return emptyList()
    }

    suspend fun getAllUserReservation(
    ): QuerySnapshot? {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            return db.collection(USERS)
                .document(email)
                .collection(RESERVATION)
                .get().await()
        }
        return null
    }

    private suspend fun getUserRatedPlaygrounds(): QuerySnapshot? {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            return db.collection(USERS)
                .document(email)
                .collection(RATING).get().await()
        }
        return null
    }


    suspend fun getAllUserReservationDates(): QuerySnapshot? {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            return db.collection(USERS)
                .document(email)
                .collection(RESERVATION).get().await()
        }
        return null
    }

    suspend fun getReservationById(reservationId: String): DocumentSnapshot? {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            return db.collection(RESERVATION)
                .document(reservationId)
                .get().await()
        }
        return null

    }

    suspend fun insertUserReservation(reservationId: String, reservation: UserReservation) {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            db.collection(USERS)
                .document(email)
                .collection(RESERVATION)
                .document(reservationId)
                .set(reservation).await()
        }
    }

    suspend fun updateUserReservation(reservationId: String, data: HashMap<String, Any>) {
        val email = currentUser.value?.email?:""

        db.collection(USERS)
            .document(email)
            .collection(RESERVATION)
            .document(reservationId)
            .set(data, SetOptions.merge()).await()
    }

    suspend fun deleteUserReservation(reservationId: String) {
        val email = currentUser.value?.email?:""

        if (email.isNotEmpty()) {
            db.collection(USERS)
                .document(email)
                .collection(RESERVATION)
                .document(reservationId)
                .delete().await()
        }


    }


    suspend fun insertReservation(reservationId: String, reservation: Reservation) {
        db.collection(RESERVATION)
            .document(reservationId)
            .set(reservation).await()
    }

    suspend fun deleteReservation(reservationId: String) {
        db.collection(RESERVATION)
            .document(reservationId)
            .delete().await()
    }

    suspend fun getTimeSlotReservationByPlaygroundAndDate(
        date: String,
        address: String,
        city: String
    )
            : QuerySnapshot {
        return db.collection(RESERVATION).get().await()
    }

    suspend fun updateReservation(
        reservationId: String,
        data: HashMap<String, Any>
    ) {

        db.collection(RESERVATION)
            .document(reservationId)
            .set(data, SetOptions.merge()).await()
    }

}