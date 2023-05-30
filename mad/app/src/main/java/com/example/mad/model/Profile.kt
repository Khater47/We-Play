package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

data class Profile(
    val description:String,
    val fullName:String,
    val nickname:String,
    val phone:String,
    val email:String
) {
    override fun toString(): String {
        return "$fullName $email $nickname $description $phone"
    }
}

fun DocumentSnapshot.toProfile(): Profile? {
    return try {

        val description:String = get("description") as String
        val fullName:String = get("fullName") as String
        val nickname:String = get("nickname") as String
        val phone:String = get("phone") as String
        val email:String = get("email") as String


        Profile(
            description,
            fullName,
            nickname,
            phone,
            email
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
