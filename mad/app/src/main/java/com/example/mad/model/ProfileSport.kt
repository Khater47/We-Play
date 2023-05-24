package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

class ProfileSport(
    val sport:String,
    val level:Int,
    val trophies:Int,
) {
    override fun toString(): String {
        return "$sport $level $trophies"
    }
}

fun DocumentSnapshot.toProfileSport(): ProfileSport? {
    return try {

        val sport:String = get("sport") as String
        val level:Int = get("level") as Int
        val trophies:Int = get("trophies") as Int

        ProfileSport(
            sport,level,trophies
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}