package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

data class Stat(
    val sport:String,
    val level:Long,
    val trophies:Long
) {
    override fun toString(): String {
        return "$sport $level $trophies\n"
    }
}

fun DocumentSnapshot.toStat(): Stat?{
    return try {
        val sport = get("sport") as String
        val level = get("level") as Long
        val trophies = get("trophies") as Long
        Stat(sport,level,trophies)

    }
    catch (e:Exception){
        e.printStackTrace()
        null
    }
}
