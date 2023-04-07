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
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.TIRAMISU)

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

        setUp() //findById all component and set action for arrow and imageButton

        //restore info from portrait to landscape
        if(savedInstanceState!=null){
            user = savedInstanceState.getStringArrayList("user")

            editFullName.setText(user?.get(0) ?:"")
            editNickname.setText(user?.get(1) ?:"")
            editDescription.setText(user?.get(2) ?:"")
            editEmail.setText(user?.get(3) ?:"")
            editPhoneNumber.setText(user?.get(4) ?:"")

        }

        //check Permission for Gallery and Camera
        if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED
            || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_DENIED){

            val permission = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission,112)
        }

    }

    //save info before move from portrait to landscape
    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)

        val fullName = editFullName.text.toString()
        val nickname = editNickname.text.toString()
        val description = editDescription.text.toString()
        val email = editEmail.text.toString()
        val phoneNumber = editPhoneNumber.text.toString()


        outState.putStringArrayList("user", arrayListOf(fullName,nickname,description,email,phoneNumber,imageUri.toString()))
    }

    //create floating menu for image button (2 option, select picture from camera or gallery)
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {

        super.onCreateContextMenu(menu, v, menuInfo)

        menuInflater.inflate(R.menu.context_menu,menu)
    }

    //handle click on camera/gallery option
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

    //save user info when click on arrow button (leave edit page)
    private fun saveUserInfo(){
        val sharedPref = getSharedPreferences("userFile", Context.MODE_PRIVATE)

        var user = JSONObject()

        user.put("fullName", if (editFullName.text.toString()=="") getString(R.string.fullName) else editFullName.text.toString())
        user.put("nickname", if(editNickname.text.toString()=="") getString(R.string.nickname) else editNickname.text.toString())
        user.put("description", if(editDescription.text.toString()=="") getString(R.string.description) else editDescription.text.toString())
        user.put("email",if(editEmail.text.toString()=="") getString(R.string.email) else editEmail.text.toString())
        user.put("phoneNumber",if(editPhoneNumber.text.toString()=="") getString(R.string.phoneNumber) else editPhoneNumber.text.toString())

        val editor = sharedPref.edit()

        editor.putString("profile",user.toString())

        editor.apply()

        saveImageOnInternalStorage()

        val intent = Intent(this, ShowProfileActivity::class.java)
        startActivity(intent)

    }

    //set image from Gallery
    private val galleryActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {

            imageUri = result.data?.data

            profilePicture.setImageURI(imageUri)

            saveImageOnInternalStorage()

        }
    }

    //launch itent for camera
    private fun openCamera(){

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        cameraActivityResultLauncher.launch(cameraIntent)

    }

    //set image from camera
    private val cameraActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val inputImage = uriToBitmap(imageUri)
            val rotated = rotateBitmap(inputImage)
            profilePicture.setImageBitmap(rotated)

            saveImageOnInternalStorage()
        }
    }

    //convert Uri to Bitmap
    private fun uriToBitmap(selectedFileUri:Uri?):Bitmap?{
        try{
            val parcelFileDescriptor =
                selectedFileUri?.let { contentResolver.openFileDescriptor(it,"r") }

            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()
            return image

        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }

    //rotate image when using landscape mode on camera
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

    //save image on local storage
    private fun saveImageOnInternalStorage(){

        val directory = filesDir
        val imageFile = File(directory, getString(R.string.imageName))

        var bitmap:Bitmap?;
        if(imageUri!=null) //select picture
            bitmap = uriToBitmap(imageUri)

        else if(imageUri==null && imageFile!=null) //set previous image, if user don't select picture
            bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

        else  //no image, set default image
          bitmap = BitmapFactory.decodeResource(this.resources,R.drawable.profile)


        val outputStream = FileOutputStream(imageFile)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    //find by id all component and set action for arrow and imageButton
    private fun setUp(){
        profilePicture = findViewById(R.id.avatar_user_profile)

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

        loadImageFromInternalStorage()

    }

    //load image from internal storage
    private fun loadImageFromInternalStorage(){

        val picture:ImageView = findViewById(R.id.avatar_user_profile)

        val fileName = getString(R.string.imageName)
        val directory = filesDir

        val imageFile = File(directory, fileName)

        if(imageFile!=null && imageFile.exists()){

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