package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

class PlaygroundRating(
    val quality:Long,
    val facilities:Long,
    val comment:String,
    val nickname:String
) {

    override fun toString(): String {
        return "$nickname $quality $facilities $comment"
    }
}


fun DocumentSnapshot.toPlaygroundRating(): PlaygroundRating? {
    return try {

        val quality:Long = get("quality") as Long
        val facilities:Long = get("facilities") as Long
        val comment:String = get("comment") as String
        val nickname:String = get("nickname") as String

        PlaygroundRating(
           quality,facilities,comment,nickname
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}