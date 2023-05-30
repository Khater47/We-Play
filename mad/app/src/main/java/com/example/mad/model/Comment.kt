package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot


data class Comment(
    val nickname: String,
    val comment: String,
    val quality:Long,
    val facilities:Long
){
    override fun toString(): String {
        return "$quality $facilities\n"
    }
}

fun DocumentSnapshot.toComment(): Comment?{
    return try {
        val nickname = get("nickname") as String
        val comment = get("comment") as String
        val quality = get("quality") as Long
        val facilities = get("facilities") as Long
        Comment(nickname,comment,quality,facilities)

    }
    catch (e:Exception){
        e.printStackTrace()
        null
    }
}
