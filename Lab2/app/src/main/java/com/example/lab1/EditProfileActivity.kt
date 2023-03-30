package com.example.lab1

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText


class EditProfileActivity : AppCompatActivity() {
    val CAMERA_ACTION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        setUp() //hide fullName and edit button
        //setUpCamera() //setUp onClick on profile picture for camera

        loadShowProfileActivity() //load showProfileActivity and pass data

    }

    /*
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==CAMERA_ACTION_CODE && resultCode== RESULT_OK && data!=null){
            val bundle:Bundle? = data.extras
            val picture = bundle?.getParcelable("data",Bitmap::class.java)
            val avatar:ImageView = findViewById(R.id.avatar_user_profile)
            avatar.setImageBitmap(picture)
        }
    }
    */

    //load showProfileActivity and pass data
    fun loadShowProfileActivity(){
        val arrowBack:ImageView  = findViewById(R.id.arrow_back_user_profile)
        arrowBack.visibility = View.VISIBLE

        arrowBack.setOnClickListener {
            val userInfo = getInfo() //get user info from text field

            //pass data from edit profile activity to show profile activity
            val intent = Intent(this, ShowProfileActivity::class.java)
            intent.putExtra("userInfo",userInfo)
            startActivity(intent)
        }

    }

    //get user input from text field
    fun getInfo():Array<String>{
        val editFullName:TextInputEditText = findViewById(R.id.edit_full_name)
        val editUsername:TextInputEditText = findViewById(R.id.edit_username)
        val editDescription:TextInputEditText = findViewById(R.id.edit_description)
        val editLocation:TextInputEditText = findViewById(R.id.edit_location)

        val userInfo = arrayOf<String>(
            editFullName.text.toString(),
            editUsername.text.toString(),
            editDescription.text.toString(),
            editLocation.text.toString()
        )

        return userInfo

    }

    //hide fullName and edit button
    fun setUp(){
        //delete fullName under user picture
        val fullName:TextView = findViewById(R.id.full_name_user_profile)
        fullName.visibility = View.GONE

        //delete editIcon in toolbar
        val editIcon:ImageView = findViewById(R.id.edit_icon_user_profile)
        editIcon.visibility = View.GONE



    }

    //image onClick -> show camera
    fun setUpCamera(){
        val avatar:ImageView = findViewById(R.id.avatar_user_profile)
        avatar.setOnClickListener {
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(packageManager)!=null){
                startActivityIfNeeded(intent,CAMERA_ACTION_CODE)
            }
            else {
                Toast.makeText(this,"There is no app that support this action",Toast.LENGTH_SHORT).show()
            }

        }
    }


}