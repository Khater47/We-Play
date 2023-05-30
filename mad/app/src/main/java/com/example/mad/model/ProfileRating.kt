package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

class ProfileRating(
    val address:String,
    val city:String,
    val playground:String,
) {
    override fun toString(): String {
        return "$address $city \n" +
                "$playground "
    }
}

fun DocumentSnapshot.toProfileRating(): ProfileRating? {
    return try {

        val address: String = get("address") as String
        val city: String = get("city") as String

        val playground: String = get("playground") as String

        ProfileRating(
            address,
            city,
            playground,

        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}