package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

class ProfileSport(
    val sport:String,
    val level:Long,
    val trophies:Long
) {
    override fun toString(): String {
        return "$sport $level $trophies"
    }
}

fun DocumentSnapshot.toProfileSport(): ProfileSport? {
    return try {

        val sport = get("sport") as String
        val level = get("level") as Long
        val trophies = get("trophies") as Long


        ProfileSport(
            sport,level,trophies
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
