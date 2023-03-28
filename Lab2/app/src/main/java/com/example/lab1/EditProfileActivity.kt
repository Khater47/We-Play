package com.example.lab1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        val fullName:TextView = findViewById(R.id.full_name_user_profile)

        fullName.visibility = View.GONE

        val arrowBack: ImageView = findViewById(R.id.arrow_back_user_profile)

        arrowBack.visibility = View.VISIBLE

        arrowBack.setOnClickListener {
            //load second activity
            val intent = Intent(this, ShowProfileActivity::class.java)
            startActivity(intent)
        }


    }



}