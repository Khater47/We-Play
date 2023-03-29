package com.example.lab1

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
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

        getUserInfo()

        loadEditProfileActivity()

        
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Reload the appropriate layout file when the device orientation changes
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_main)
        else
            setContentView(R.layout.activity_main)

    }

    fun loadEditProfileActivity(){
        val editIcon: ImageView = findViewById(R.id.edit_icon_user_profile)
        editIcon.setOnClickListener {
            //load second activity
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

    }
    fun getUserInfo(){
        val userInfo = intent.getStringArrayExtra("userInfo")
        //print("-------------------------->USERINFO MAIN"+ (userInfo?.get(0) ?: "null"))

        if(userInfo!=null){

            val fullName: TextView = findViewById(R.id.full_name_user_profile)
            val userName: TextView = findViewById(R.id.custom_username_user_profile)
            val description: TextView = findViewById(R.id.custom_description_user_profile)
            val location: TextView = findViewById(R.id.custom_location_user_profile)

            fullName.text = userInfo[0].toString()
            userName.text = userInfo[1].toString()
            description.text = userInfo[2].toString()
            location.text = userInfo[3].toString()

        }



    }



    
}