package com.example.lab1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.result.contract.ActivityResultContracts
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialog


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class EditProfileActivity : AppCompatActivity() {

    private lateinit var profilePicture: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        profilePicture = findViewById(R.id.avatar_user_profile)

        //delete editIcon in toolbar
        val editIcon:ImageView = findViewById(R.id.edit_icon_user_profile)
        editIcon.visibility = View.GONE

        takePicture()
        loadShowProfileActivity() //load showProfileActivity and pass data

    }

    //load showProfileActivity and pass data
    private fun loadShowProfileActivity(){
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

    /**get user input from text field*/
    private fun getInfo(): Array<String> {

        val editFullName: TextInputEditText = findViewById(R.id.edit_full_name)
        val editUsername: TextInputEditText = findViewById(R.id.edit_username)
        val editDescription: TextInputEditText = findViewById(R.id.edit_description)
        val editLocation: TextInputEditText = findViewById(R.id.edit_location)

        return arrayOf(
            editFullName.text.toString(),
            editUsername.text.toString(),
            editDescription.text.toString(),
            editLocation.text.toString()
        )

    }

    /**Dialog for selected picture from gallery/camera*/
    private fun takePicture() {

        if (checkCameraHardware(this)){
            profilePicture.setOnClickListener {

                val imageDialog = BottomSheetDialog(this)
                val dialogPictureLayout = findViewById<LinearLayout>(R.id.dialogPictureLayout)
                val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.take_picture, dialogPictureLayout)

                // set the click listeners for the camera and gallery options
                bottomSheetView.findViewById<TextView>(R.id.camera_btn).setOnClickListener {
                    // handle camera option click
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
        else
            Toast.makeText(this,"This don't have a camera",Toast.LENGTH_SHORT).show()
    }

    /**Intent camera for getting picture and show on user profile imageView*/
    private val cameraActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val bundle: Bundle? = result.data?.extras

            val size = profilePicture.width

            val picture = bundle?.getParcelable("data",Bitmap::class.java)

            val resizePicture = Bitmap.createScaledBitmap(picture as Bitmap, size, size, true)

            profilePicture.setImageBitmap(resizePicture)

        }
    }

    /**Intent gallery for getting picture and show on user profile imageView*/
    private val galleryActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {

            val selectedImageUri: Uri? = result.data?.data
            // Handle the selected image URI here
            //val profilePicture: ImageView = findViewById(R.id.avatar_user_profile)

            profilePicture.setImageURI(selectedImageUri)
        }
    }

    /** Check if this device has at least 1 camera */
    private fun checkCameraHardware(context: Context): Boolean {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true
        }
        return false
    }



}