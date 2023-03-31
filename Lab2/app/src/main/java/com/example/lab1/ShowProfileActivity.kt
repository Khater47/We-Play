package com.example.lab1

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
//import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class ShowProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initProfile()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.show_profile_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.edit_button -> {

                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun initProfile(){
        val userInfo = intent.getStringArrayExtra("user")

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



        }
        else
            Toast.makeText(this,"NULL",Toast.LENGTH_SHORT).show()

    }


}


/*
    <include layout="@layout/toolbar" />


            vm.addUser(user)

            vm.users.observe(this){
                fullName.text = user.fullName
                userName.text = user.username
                description.text = user.description
                location.text = user.location
            }

             private fun loadEditProfileActivity(){
        val editIcon: ImageView = findViewById(R.id.edit_icon_user_profile)
        editIcon.setOnClickListener {
            //load edit profile activity
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

    }
            */
