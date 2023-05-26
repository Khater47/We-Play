package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

class Playground(
    val playground:String,
    val sport:String,
    val city:String,
    val address:String
){
    override fun toString(): String {
        return "$playground $sport $city $address"
    }


}

fun DocumentSnapshot.toPlayground(): Playground?{
    return try {
        val playground = get("playground") as String
        val sport = get("sport") as String
        val city = get("city") as String
        val address = get("address") as String
        Playground(playground,sport,city,address)

    }
    catch (e:Exception){
        e.printStackTrace()
        null
    }
}

