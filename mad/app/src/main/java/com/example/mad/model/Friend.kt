package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

data class Friend(
    val email:String,
    val nickname:String,
    val fullName:String
) {
    override fun toString(): String {
        return "$email $nickname $fullName\n"
    }
}


fun DocumentSnapshot.toFriend(): Friend?{
    return try {
        val nickname = get("nickname") as String
        val fullName = get("fullName") as String
        val email = get("email") as String
        Friend(email,nickname, fullName)

    }
    catch (e:Exception){
        e.printStackTrace()
        null
    }
}
