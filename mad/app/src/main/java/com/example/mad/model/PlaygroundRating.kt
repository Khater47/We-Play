package com.example.mad.model

import com.google.firebase.firestore.DocumentSnapshot

class PlaygroundRating(
    val quality:Float,
    val facilities:Float,
    val comment:Map<String,String>
) {

}
