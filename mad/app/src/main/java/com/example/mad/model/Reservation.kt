package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

class Reservation(
    val address: String,
    val city: String,
    val date: String,
    val email: String,
    val endTime: String,
    val equipment: Boolean,
    val fullName: String,
    val phone: String,
    val playground: String,
    val sport: String,
    val startTime: String,
) {
    override fun toString(): String {
        return "$address $city $date $email\n" +
                " $startTime-$endTime $equipment $fullName\n" +
                "$phone $playground $sport "
    }
}

fun DocumentSnapshot.toReservation(): Reservation? {
    return try {

        val address: String = get("address") as String
        val city: String = get("city") as String
        val date: String = get("date") as String
        val email: String = get("email") as String
        val endTime: String = get("endTime") as String
        val equipment: Boolean = get("equipment") as Boolean
        val fullName: String = get("fullName") as String
        val phone: String = get("phone") as String
        val playground: String = get("playground") as String
        val sport: String = get("sport") as String
        val startTime: String = get("startTime") as String
        Reservation(
            address,
            city,
            date,
            email,
            endTime,
            equipment,
            fullName,
            phone,
            playground,
            sport,
            startTime,
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
