package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

class Playground(
    val id:String,
    val phone:String,
    val email:String,
    val openHours:String,
    val playground:String,
    val sport:String,
    val city:String,
    val address:String
){
    override fun toString(): String {
        return "$id $playground $sport $city $address"+
                "$phone $email  $openHours"
    }


}

fun DocumentSnapshot.toPlayground(): Playground?{
    return try {
        val playground = get("playground") as String
        val openHours = get("openHours") as String
        val phone = get("phone") as String
        val email = get("email") as String
        val sport = get("sport") as String
        val city = get("city") as String
        val address = get("address") as String
        Playground(id,phone,email,openHours,playground,sport,city,address)

    }
    catch (e:Exception){
        e.printStackTrace()
        null
    }
}

