package com.example.lab1

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class ShowProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //delete default app title

        // Load the appropriate layout file based on the device orientation
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_main)
        else
            setContentView(R.layout.activity_main)



        val editIcon: ImageView = findViewById(R.id.edit_icon_user_profile)
        editIcon.setOnClickListener {
            //load second activity
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }


        
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Reload the appropriate layout file when the device orientation changes
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_main)
        else
            setContentView(R.layout.activity_main)

    }


    
}