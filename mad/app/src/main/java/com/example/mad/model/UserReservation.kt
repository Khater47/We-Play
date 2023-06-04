package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

class UserReservation(
    val address:String,
    val city:String,
    val date:String,
    val endTime:String,
    val equipment:Boolean,
    val id:String,
    val playground:String,
    val sport:String,
    val startTime:String,
) {
    override fun toString(): String {
        return "$id $address $city $date \n" +
                "$startTime-$endTime $equipment \n" +
                "$playground $sport "
    }
}

fun DocumentSnapshot.toUserReservation(): UserReservation? {
    return try {

        val address: String = get("address") as String
        val id: String = get("id") as String
        val city: String = get("city") as String
        val date: String = get("date") as String
        val endTime: String = get("endTime") as String
        val equipment: Boolean = get("equipment") as Boolean
        val playground: String = get("playground") as String
        val sport: String = get("sport") as String
        val startTime: String = get("startTime") as String
        UserReservation(
            address,
            city,
            date,
            endTime,
            equipment,
            id,
            playground,
            sport,
            startTime,
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun UserReservation.toReservation():Reservation{

    return Reservation(
        address,
        city,
        date,
        "",
        endTime,
        equipment,
        id,
        playground,
        sport,
        startTime,
    )
}