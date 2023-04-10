package com.example.mad

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject

const val PERMISSION = 112

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

        //Permission denied => request run time permission for Gallery and Camera
        if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED
            || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_DENIED){

            val permission = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission,PERMISSION)
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
            R.id.cameraMenu -> {

                if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.CAMERA)
                    requestPermissions( permission, PERMISSION)
                }
                else openCamera()

                true
            }
            R.id.galleryMenu -> {

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

        val user = JSONObject()

        var value: String = getEditString(editFullName.text.toString(),sharedPref,"fullName")
        user.put("fullName",value)

        value=getEditString(editNickname.text.toString(),sharedPref,"nickname")
        user.put("nickname",value)

        value=getEditString(editDescription.text.toString(),sharedPref,"description")
        user.put("description",value)

        value=getEditString(editEmail.text.toString(),sharedPref,"email")
        user.put("email",value)

        value=getEditString(editPhoneNumber.text.toString(),sharedPref,"phoneNumber")
        user.put("phoneNumber",value)


        val editor = sharedPref.edit()

        editor.putString("profile",user.toString())

        editor.apply()

        saveImageOnInternalStorage(imageUri,this)

        val intent = Intent(this, ShowProfileActivity::class.java)
        startActivity(intent)

    }

    //if editString!="" return editText else return previousUserString
    private fun getEditString(editString:String, sharedPref: SharedPreferences, key:String):String{
        var prevUser: JSONObject? = null

        if(sharedPref.getString("profile",null)!=null)
            prevUser = JSONObject(sharedPref.getString("profile",null) as String)

        return if(editString=="")
            prevUser?.getString(key)?:""
        else
            editString

    }

    //set image from Gallery and save image on local storage
    private val galleryActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {

            imageUri = result.data?.data

            profilePicture.setImageURI(imageUri)

            saveImageOnInternalStorage(imageUri,this)

        }
    }

    //launch intent for camera
    private fun openCamera(){

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        cameraActivityResultLauncher.launch(cameraIntent)

    }

    //set image from camera and save image on local storage
    private val cameraActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val inputImage = uriToBitmap(imageUri,this)
            val rotated = rotateBitmap(inputImage,imageUri,this)
            profilePicture.setImageBitmap(rotated)

            saveImageOnInternalStorage(imageUri,this)

        }
    }

    //find by id all component and set action for arrow and imageButton
    private fun setUp(){
        profilePicture = findViewById(R.id.userProfilePicture)

        profileButton = findViewById(R.id.imageButton)

        editFullName = findViewById(R.id.editFullName)
        editNickname = findViewById(R.id.editNickname)
        editDescription = findViewById(R.id.editDescription)
        editEmail = findViewById(R.id.editEmail)
        editPhoneNumber = findViewById(R.id.editPhoneNumber)

        arrowBack = findViewById(R.id.arrowBackUserProfile)
        arrowBack.setOnClickListener {
            saveUserInfo()
        }

        registerForContextMenu(profileButton)

        val imageBitmap = getImageFromInternalStorage(this)
        profilePicture.setImageBitmap(imageBitmap)

    }



}