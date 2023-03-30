package com.example.lab1

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.result.contract.ActivityResultContracts
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog


class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        setUp() //hide fullName and edit button
        takePicture()
        loadShowProfileActivity() //load showProfileActivity and pass data

    }


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

    fun takePicture() {
        val avatar:ImageView = findViewById(R.id.avatar_user_profile)
        avatar.setOnClickListener {

            val imageDialog = BottomSheetDialog(this)
            val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.take_picture, null)

            // set the click listeners for the camera and gallery options
            bottomSheetView.findViewById<TextView>(R.id.camera_btn).setOnClickListener {
                // handle camera option click
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                cameraActivityResultLauncher.launch(cameraIntent)
                imageDialog.dismiss()
            }

            bottomSheetView.findViewById<TextView>(R.id.gallery_btn).setOnClickListener {
                // handle gallery option click
                val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                val mimeTypes = arrayOf("image/*")
                pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                galleryActivityLauncher.launch(pickIntent)

                imageDialog.dismiss()
            }

            // set the view for the BottomSheetDialog and show it
            imageDialog.setContentView(bottomSheetView)
            imageDialog.show()
        }
    }

    private val cameraActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bundle: Bundle? = result.data?.extras

            val profilePicture: ImageView = findViewById(R.id.avatar_user_profile)
            val picture = bundle?.getParcelable<Bitmap>("data")

            val size = profilePicture.width
            val resizePicture = Bitmap.createScaledBitmap(picture as Bitmap, size, size, true)

            profilePicture.setImageBitmap(resizePicture)

        }
    }

    val galleryActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val selectedImageUri: Uri? = result.data?.data
            // Handle the selected image URI here
            val profilePicture: ImageView = findViewById(R.id.avatar_user_profile)


            profilePicture.setImageURI(selectedImageUri)
        }
    }


}