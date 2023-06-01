package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

data class Notification(
    val address:String,
    val city:String,
    val date:String,
    val endTime:String,
    val equipment:Boolean,
    val id:String,
    val playground:String,
    val sport:String,
    val startTime:String,
    val fullName:String,
    val level:Long,
    val trophies:Long
) {
    override fun toString(): String {
        return "$id $address $city $date \n" +
                "$startTime-$endTime $equipment \n" +
                "$playground $sport $fullName $level $trophies"
    }
}

fun DocumentSnapshot.toNotification(): Notification? {
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

        val fullName: String = get("fullName") as String
        val level: Long = get("level") as Long
        val trophies: Long = get("trophies") as Long

        Notification(
            address,
            city,
            date,
            endTime,
            equipment,
            id,
            playground,
            sport,
            startTime,
            fullName,
            level,
            trophies
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}