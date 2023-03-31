package com.example.lab1

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.*

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

        val fullName: TextView = findViewById(R.id.full_name_user_profile)
        val userName: TextView = findViewById(R.id.custom_username_user_profile)
        val description: TextView = findViewById(R.id.custom_description_user_profile)
        val location: TextView = findViewById(R.id.custom_location_user_profile)

        val sharedPref = getSharedPreferences("userFile",Context.MODE_PRIVATE)

        //Load User Info
        if(sharedPref!=null){

            fullName.text = sharedPref.getString("fullName",getString(R.string.fullName))
            userName.text = sharedPref.getString("username",getString(R.string.username))
            description.text = sharedPref.getString("description",getString(R.string.description))
            location.text = sharedPref.getString("location",getString(R.string.location))

        }

        //Load User Profile Picture
        loadImageFromInternalStorage()


    }

    private fun loadImageFromInternalStorage(){

        val picture:ImageView = findViewById(R.id.avatar_user_profile)

        val fileName = getString(R.string.imageName)
        val directory = filesDir // directory privata dell'app

        val imageFile = File(directory, fileName)

        if(imageFile!=null){
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            picture.setImageBitmap(bitmap)
        }
    }

}

