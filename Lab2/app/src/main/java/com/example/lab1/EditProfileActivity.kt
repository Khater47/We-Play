package com.example.lab1

//import androidx.activity.viewModels
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialog


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class EditProfileActivity : AppCompatActivity() {

    private lateinit var profilePicture: ImageView
    private lateinit var editFullName:TextInputEditText;
    private lateinit var editUsername:TextInputEditText;
    private lateinit var editDescription:TextInputEditText;
    private lateinit var editLocation:TextInputEditText;

    private var user:ArrayList<String>? = arrayListOf()

    //private val vm  by viewModels<ShowProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        setUp() //initialize lateinit var

        takePicture()
        //loadShowProfileActivity() //load showProfileActivity and pass data

        if(savedInstanceState!=null){
            user = savedInstanceState.getStringArrayList("user")
            setEditText()
        }
    }

    private fun setUp(){
        profilePicture = findViewById(R.id.avatar_user_profile)

        editFullName = findViewById(R.id.edit_full_name)
         editUsername = findViewById(R.id.edit_username)
         editDescription = findViewById(R.id.edit_description)
         editLocation = findViewById(R.id.edit_location)

    }

    private fun setEditText(){
        editFullName.setText(user?.get(0) ?:"")
        editUsername.setText(user?.get(1) ?:"")
        editDescription.setText(user?.get(2) ?:"")
        editLocation.setText(user?.get(3) ?:"")
    }
    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)

        val fullName = editFullName.text.toString()
        val username = editUsername.text.toString()
        val description = editDescription.text.toString()
        val location = editLocation.text.toString()

        outState.putStringArrayList("user", arrayListOf(fullName,username,description,location))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.save_button -> {

                val array:Array<String> = arrayOf(
                    editFullName.text.toString(),
                    editUsername.text.toString(),
                    editDescription.text.toString(),
                    editLocation.text.toString(),
                    )
                //pass data from edit profile activity to show profile activity
                val intent = Intent(this, ShowProfileActivity::class.java)
                intent.putExtra("user",array)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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