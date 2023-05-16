package com.example.mad.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.SportsGolf
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.example.mad.R
import java.io.File
import java.io.FileOutputStream

fun getIconSport(sportText: String): ImageVector {
    return when (sportText.lowercase()) {
        "soccer" -> Icons.Default.SportsSoccer
        "baseball" -> Icons.Default.SportsBaseball
        "basketball" -> Icons.Default.SportsBasketball
        "cricket" -> Icons.Default.SportsCricket
        "football" -> Icons.Default.SportsFootball
        "golf" -> Icons.Default.SportsGolf
        "gymnastic" -> Icons.Default.SportsGymnastics
        "tennis" -> Icons.Default.SportsTennis
        else -> Icons.Default.SportsVolleyball

    }
}

fun getIconUserInfo(userInfo: String): ImageVector {
    return when (userInfo) {
        "FullName" -> Icons.Default.Person
        "Email" -> Icons.Default.Email
        "Nickname" -> Icons.Default.AlternateEmail
        "Description" -> Icons.Default.Description
        else -> Icons.Default.Phone

    }
}


fun getKeyboard(userInfo: String): KeyboardType {
    return when (userInfo) {
        "PhoneNumber" -> KeyboardType.Number
        "Email" -> KeyboardType.Email
        else -> KeyboardType.Text
    }
}


const val QUALITY = 100

//convert Uri to Bitmap
fun uriToBitmap(selectedFileUri: Uri?, context: Context): Bitmap? {
    try {
        val parcelFileDescriptor =
            selectedFileUri?.let { context.contentResolver.openFileDescriptor(it, "r") }

        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

//rotate image when using landscape mode on camera
@SuppressLint("Range")
fun rotateBitmap(input: Bitmap?, imageUri: Uri?, context: Context): Bitmap? {
    val orientationColumn: Array<String> = arrayOf(MediaStore.Images.Media.ORIENTATION)
    val cur: Cursor? =
        imageUri?.let { context.contentResolver.query(it, orientationColumn, null, null, null) }
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

@RequiresApi(Build.VERSION_CODES.P)
fun saveImageUriOnInternalStorage(imageUri: Uri?, context: Context): Bitmap {

    val directory = context.filesDir
    val imageFile = File(directory, "profilePicture")

    val bitmap = if (imageUri != null) //select picture
    {
        val tempBitmap = uriToBitmap(imageUri, context)
        rotateBitmap(tempBitmap, imageUri, context)
    } else if (imageFile.totalSpace > 0) //set previous image, if user don't select picture
        BitmapFactory.decodeFile(imageFile.absolutePath)
    else  //no image provided, set default image
        BitmapFactory.decodeResource(context.resources, R.drawable.profile)


    Log.d("TAG - ", bitmap.toString())

    val outputStream = FileOutputStream(imageFile)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream)
    outputStream.flush()
    outputStream.close()
    return bitmap ?: BitmapFactory.decodeResource(context.resources, R.drawable.profile)
}


fun getImageFromInternalStorage(context: Context): Bitmap {

    val fileName = "profilePicture"
    val directory = context.filesDir

    val imageFile = File(directory, fileName)

    return if (imageFile.totalSpace > 0)

    //get selected image
        BitmapFactory.decodeFile(imageFile.absolutePath)
            ?: BitmapFactory.decodeResource(context.resources, R.drawable.profile)


    //get default image
    else
        BitmapFactory.decodeResource(context.resources, R.drawable.profile)

}

fun invalidField(user: Bundle): Boolean {

    if (user.getString("FullName") == null || user.getString("FullName") == "") {

        return true
    }
    if (user.getString("Email") == null || user.getString("Email") == "") {


        return true
    }
    if (user.getString("Nickname") == null || user.getString("Nickname") == "") {

        return true
    }
    if (user.getString("PhoneNumber") == null || user.getString("PhoneNumber") == "") {

        return true

    }
    if (user.getString("Description") == null || user.getString("Description") == "") {

        return true
    }
    return false
}

fun convertAchievement(achievement: Int): String? {
    return when (achievement) {
        1-> {
            "first"
        }

        2 -> {
            "second"
        }

        3 -> {
            "third"
        }

        else -> {
            null
        }
    }
}

fun getColorFromAchievement(achievement:String): Color {

    return when(achievement){
        "first"-> Color(0xFFFFB600)
        "second"-> Color.Gray
        else ->  Color(0xFFCD7F32)
    }
}


fun getIconPlayground(sport: String): Int {
    return when (sport.lowercase()) {
        "soccer" -> R.drawable.soccer
        "baseball" -> R.drawable.baseball
        "basketball" -> R.drawable.basketball
        "cricket" -> R.drawable.cricket
        "football" -> R.drawable.football
        "golf" -> R.drawable.golf
        "gymnastic" -> R.drawable.gymnastic
        "tennis" -> R.drawable.tennis
        else -> R.drawable.volleyball

    }
}



//launch intent for camera
fun openCamera(
    context: Context,
    cameraActivityResultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
): Uri? {

    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, "New Picture")
    values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
    val imageUri =
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
    cameraActivityResultLauncher.launch(cameraIntent)
    return imageUri
}

fun openGallery(
    galleryActivityResultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    val mimeTypes = arrayOf("image/*")
    galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    galleryActivityResultLauncher.launch(galleryIntent)
}