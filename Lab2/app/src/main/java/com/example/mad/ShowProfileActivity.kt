package com.example.mad

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.*


@RequiresApi(Build.VERSION_CODES.TIRAMISU)

class ShowProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initProfile()

    }

    //create edit button on toolBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.show_profile_menu,menu)
        return true
    }

    //handle edit button click, intent = EditActivityProfile
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.edit_button -> {

                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //load User Info
    private fun initProfile(){

        val fullName: TextView = findViewById(R.id.fullNameUserProfile)
        val nickname: TextView = findViewById(R.id.customNicknameUserProfile)
        val description: TextView = findViewById(R.id.customDescriptionUserProfile)
        val email: TextView = findViewById(R.id.emailUserProfile)
        val phoneNumber: TextView = findViewById(R.id.customPhoneNumberUserProfile)

        val sharedPref = getSharedPreferences("userFile",Context.MODE_PRIVATE)

        //Load User Info
        if(sharedPref!=null){

            val userString = sharedPref.getString("profile",null)

            if(userString!=null){

                val user = JSONObject(userString)

                fullName.text = user.get("fullName").toString()
                nickname.text = user.get("nickname").toString()
                description.text = user.get("description").toString()
                email.text = user.get("email").toString()
                phoneNumber.text = user.get("phoneNumber").toString()

            }

        }

        //Load User Profile Picture
        loadImageFromInternalStorage()

    }

    //load image from internal storage
    private fun loadImageFromInternalStorage(){

        val picture:ImageView = findViewById(R.id.userProfilePicture)

        val fileName = getString(R.string.imageName)
        val directory = filesDir

        val imageFile = File(directory, fileName)

        if(imageFile!=null && imageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                picture.setImageBitmap(bitmap)

        }

        //load default image
        else{
            val imageProfileDefault = BitmapFactory.decodeResource(resources,R.drawable.profile)
            picture.setImageBitmap(imageProfileDefault)
        }

    }


}

