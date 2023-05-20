package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class Playground(
    val playground:String,
    val sport:String,
    val location:String
){
    override fun toString(): String {
        return "$playground $sport $location"
    }


}

fun DocumentSnapshot.toPlayground(): Playground?{
    return try {
        val playground = get("playground") as String
        val sport = get("sport") as String
        val location = get("location") as String
        Playground(playground,sport,location)

    }
    catch (e:Exception){
        e.printStackTrace()
        null
    }
}

