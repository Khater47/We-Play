package com.example.lab1

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import org.json.JSONStringer
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

    //load User Info
    private fun initProfile(){

        val fullName: TextView = findViewById(R.id.full_name_user_profile)
        val nickname: TextView = findViewById(R.id.custom_nickname_user_profile)
        val description: TextView = findViewById(R.id.custom_description_user_profile)
        val email: TextView = findViewById(R.id.email_user_profile)
        val phoneNumber: TextView = findViewById(R.id.custom_phone_number_user_profile)

        val sharedPref = getSharedPreferences("userFile",Context.MODE_PRIVATE)

        //Load User Info
        if(sharedPref!=null){

            val obj = JSONObject(sharedPref.getString("user",null))
            val objFullName=obj.get("fullName").toString()
            val objNickname=obj.get("nickname").toString()
            val objDescription=obj.get("description").toString()
            val objEmail=obj.get("email").toString()
            val objPhoneNumber=obj.get("phoneNumber").toString()

            //remove [" "]
            fullName.text = objFullName.substring(2,objFullName.length-2)
            nickname.text = objNickname.substring(2,objNickname.length-2)
            description.text = objDescription.substring(2,objDescription.length-2)
            email.text = objEmail.substring(2,objEmail.length-2)
            phoneNumber.text = objPhoneNumber.substring(2,objPhoneNumber.length-2)

        }

        //Load User Profile Picture
        loadImageFromInternalStorage()


    }

    //load image from internal storage
    private fun loadImageFromInternalStorage(){

        val picture:ImageView = findViewById(R.id.avatar_user_profile)

        val fileName = getString(R.string.imageName)
        val directory = filesDir

        val imageFile = File(directory, fileName)

        if(imageFile!=null) {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            picture.setImageBitmap(bitmap)
        }
    }


}

