package com.example.mad.model

class Reservation(
    val address:String,
    val city:String,
    val date:String,
    val email:String,
    val endTime:String,
    val equipment:Boolean,
    val fullName:String,
    val phone:String,
    val playground:String,
    val sport:String,
    val startTime:String
) {
    override fun toString(): String {
        return "$address $city $date $email\n" +
                " $startTime-$endTime $equipment $fullName\n" +
                "$phone $playground $sport"
    }
}