package com.example.lab1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class ShowProfileActivity : AppCompatActivity() {
    private val vm  by viewModels<ShowProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initProfile()

        loadEditProfileActivity()
    }

    fun initProfile(){
        val userInfo = intent.getStringArrayExtra("userInfo")

        if(userInfo!=null){

            val fullName: TextView = findViewById(R.id.full_name_user_profile)
            val userName: TextView = findViewById(R.id.custom_username_user_profile)
            val description: TextView = findViewById(R.id.custom_description_user_profile)
            val location: TextView = findViewById(R.id.custom_location_user_profile)

            val user = User(userInfo[0].toString(),userInfo[1].toString(),userInfo[2].toString(),userInfo[3].toString())

            fullName.text = user.fullName
            userName.text = user.username
            description.text = user.description
            location.text = user.location


            vm.addUser(user)

            vm.users.observe(this){
                fullName.text = user.fullName
                userName.text = user.username
                description.text = user.description
                location.text = user.location
            }


        }

    }
    fun loadEditProfileActivity(){
        val editIcon: ImageView = findViewById(R.id.edit_icon_user_profile)
        editIcon.setOnClickListener {
            //load edit profile activity
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

    }

}