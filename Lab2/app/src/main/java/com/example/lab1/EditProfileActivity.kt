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
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class EditProfileActivity : AppCompatActivity() {

    private lateinit var profilePicture: ImageView
    private lateinit var editFullName:TextInputEditText
    private lateinit var editNickname:TextInputEditText
    private lateinit var editDescription:TextInputEditText
    private lateinit var editEmail:TextInputEditText
    private lateinit var editPhoneNumber:TextInputEditText
    private lateinit var arrowBack:ImageView
    private lateinit var profileButton: TextView


    private var user:ArrayList<String>? = arrayListOf()
    private var imageUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        setUp() //initialize var and set onClickListener for select picture


        if(savedInstanceState!=null){
            user = savedInstanceState.getStringArrayList("user")
            setEditText()
            loadImageFromInternalStorage()
        }


        if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED
            || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_DENIED){

            val permission = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission,112)
        }

    }

    private fun saveUserInfo(){
        val sharedPref = getSharedPreferences("userFile", Context.MODE_PRIVATE)

        val editor = sharedPref.edit()
        editor.putString("fullName", editFullName.text.toString())
        editor.putString("nickname", editNickname.text.toString())
        editor.putString("description", editDescription.text.toString())
        editor.putString("email", editEmail.text.toString())
        editor.putString("phoneNumber", editPhoneNumber.text.toString())
        editor.apply()

        val intent = Intent(this, ShowProfileActivity::class.java)
        startActivity(intent)

    }
    private fun setUp(){
        profilePicture = findViewById(R.id.avatar_user_profile)

        //profileButton.setOnClickListener { pictureDialog() }
        profileButton = findViewById(R.id.image_button)

        editFullName = findViewById(R.id.edit_full_name)
         editNickname = findViewById(R.id.edit_nickname)
         editDescription = findViewById(R.id.edit_description)
         editEmail = findViewById(R.id.edit_email)
        editPhoneNumber = findViewById(R.id.edit_phoneNumber)

        arrowBack = findViewById(R.id.arrow_back_user_profile)
        arrowBack.setOnClickListener {
            saveUserInfo()
        }

        registerForContextMenu(profileButton)

    }
    private fun setEditText(){
        editFullName.setText(user?.get(0) ?:"")
        editNickname.setText(user?.get(1) ?:"")
        editDescription.setText(user?.get(2) ?:"")
        editEmail.setText(user?.get(3) ?:"")
        editPhoneNumber.setText(user?.get(4) ?:"")

    }
    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)

        val fullName = editFullName.text.toString()
        val nickname = editNickname.text.toString()
        val description = editDescription.text.toString()
        val email = editEmail.text.toString()
        val phoneNumber = editPhoneNumber.text.toString()


        outState.putStringArrayList("user", arrayListOf(fullName,nickname,description,email))
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {

        super.onCreateContextMenu(menu, v, menuInfo)

        menuInflater.inflate(R.menu.context_menu,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.camera_menu -> {

                if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.CAMERA)
                    requestPermissions( permission, 112)
                }
                else openCamera()


                true
            }
            R.id.gallery_menu -> {

                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                val mimeTypes = arrayOf("image/*")
                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                galleryActivityResultLauncher.launch(galleryIntent)

                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private val galleryActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {

            imageUri = result.data?.data
            // Handle the selected image URI here

            profilePicture.setImageURI(imageUri)

            saveImageOnInternalStorage()
        }
    }

    private fun openCamera(){

        val values = ContentValues()
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

            saveImageOnInternalStorage()

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
    private fun rotateBitmap(input: Bitmap?): Bitmap? {
        val orientationColumn: Array<String> = arrayOf(MediaStore.Images.Media.ORIENTATION)
        val cur: Cursor? =
            imageUri?.let { contentResolver.query(it, orientationColumn, null, null, null) }
        var orientation = -1
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
        }
        Log.d("tryOrientation", orientation.toString() + "")
        val rotationMatrix = Matrix()
        rotationMatrix.setRotate(orientation.toFloat())
        return input?.let {
            Bitmap.createBitmap(
                it,
                0,
                0,
                input.width,
                input.height,
                rotationMatrix,
                true
            )
        }
    }

    private fun saveImageOnInternalStorage(){

        val directory = filesDir
        val imageFile = File(directory, getString(R.string.imageName))

        val bitmap = uriToBitmap(imageUri)
        val outputStream = FileOutputStream(imageFile)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    private fun loadImageFromInternalStorage(){

        val picture:ImageView = findViewById(R.id.avatar_user_profile)

        val fileName = getString(R.string.imageName)
        val directory = filesDir

        val imageFile = File(directory, fileName)

        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        picture.setImageBitmap(bitmap)

    }
}