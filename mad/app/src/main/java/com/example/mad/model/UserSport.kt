package com.example.mad.model

class UserSport(
    val sport:String,
    val level:Int,
    val trophies:Int
) {
    override fun toString(): String {
        return "$sport $level $trophies"
    }
}