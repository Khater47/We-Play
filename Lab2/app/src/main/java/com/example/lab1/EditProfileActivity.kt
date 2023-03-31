package com.example.lab1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.IOException
import java.util.jar.Manifest


class EditProfileActivity : AppCompatActivity() {

    private lateinit var profilePicture: ImageView
    private lateinit var editFullName:TextInputEditText;
    private lateinit var editUsername:TextInputEditText;
    private lateinit var editDescription:TextInputEditText;
    private lateinit var editLocation:TextInputEditText;

    private var user:ArrayList<String>? = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        setUp() //initialize lateinit var and set onClickListener for select picture

        if(savedInstanceState!=null){
            user = savedInstanceState.getStringArrayList("user")
            setEditText()
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED
                || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_DENIED){

                val permission = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission,112)
            }
        }
    }

    private fun setUp(){
        profilePicture = findViewById(R.id.avatar_user_profile)

        profilePicture.setOnClickListener { PictureDialog() }

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

    fun PictureDialog(){

        val imageDialog = BottomSheetDialog(this)
        val dialogPictureLayout = findViewById<LinearLayout>(R.id.dialogPictureLayout)
        val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.take_picture, dialogPictureLayout)

        // set the click listeners for the camera and gallery options
        bottomSheetView.findViewById<TextView>(R.id.camera_btn).setOnClickListener {
            // handle camera option click


            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.CAMERA)
                    requestPermissions( permission, 112)
                }
                else openCamera()
            }
            else openCamera()

            imageDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.gallery_btn).setOnClickListener {
            // handle gallery option click
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val mimeTypes = arrayOf("image/*")
            galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            galleryActivityResultLauncher.launch(galleryIntent)

            imageDialog.dismiss()
        }

        // set the view for the BottomSheetDialog and show it
        imageDialog.setContentView(bottomSheetView)
        imageDialog.show()
    }

    private val galleryActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {

            val selectedImageUri: Uri? = result.data?.data
            // Handle the selected image URI here

            profilePicture.setImageURI(selectedImageUri)
        }
    }

    private var imageUri:Uri? = null;
    private fun openCamera(){

        val values:ContentValues = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        cameraActivityResultLauncher.launch(cameraIntent)

    }

    private val cameraActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val inputImage = uriToBitmap(imageUri)
            val rotated = rotateBitmap(inputImage)
            profilePicture.setImageBitmap(rotated)

        }
    }

    private fun uriToBitmap(selectedFileUri:Uri?):Bitmap?{
        try{
            val parcelFileDescriptor =
                selectedFileUri?.let { contentResolver.openFileDescriptor(it,"r") }

            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)

            parcelFileDescriptor?.close()
            return image
        }
        catch (e:IOException){
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("Range")
    fun rotateBitmap(input:Bitmap?): Bitmap? {
        val orientationColumn:Array<String> = arrayOf(MediaStore.Images.Media.ORIENTATION)
        val cur: Cursor? = imageUri?.let { contentResolver.query(it,orientationColumn,null,null,null) }
        var orientation=-1
        if (cur != null && cur.moveToFirst()){
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
        }
        Log.d("tryOrientation",orientation.toString()+"")
        val rotationMatrix = Matrix()
        rotationMatrix.setRotate(orientation.toFloat())
        val cropped =
            input?.let { Bitmap.createBitmap(it,0,0,input.width,input.height,rotationMatrix,true) }
        return cropped
    }
}