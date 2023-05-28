package com.example.mad.common

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.example.mad.R
import java.io.File
import java.io.FileOutputStream
import java.util.Locale


fun formatDate(day: Int, month: Int, year: Int): String {
    val m = if (month < 9) "0${month + 1}" else "${month + 1}"
    val d = if (day < 10) "0${day}" else "$day"
    return "$d/$m/$year"
}

fun getToday():String{
    val calendar = Calendar.getInstance(Locale.ITALY)
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    return formatDate(day, month, year)
}

//fun getMonth(): String {
//
//    val month = Calendar.getInstance(Locale.ITALY).get(Calendar.MONTH) + 1
//    if(month<10) return "0$month"
//    return "$month"
//}

fun getTimeStamp():Long{
    return Calendar.getInstance().timeInMillis
}
fun getIconUserInfo(userInfo: String): ImageVector {
    return when (userInfo.lowercase()) {
        "fullName" -> Icons.Default.Person
        "email" -> Icons.Default.Email
        "nickname" -> Icons.Default.AlternateEmail
        "description" -> Icons.Default.Description
        "password" -> Icons.Default.Lock
        else -> Icons.Default.Phone

    }
}

fun validationTextField(type:String,text:String):Boolean{
    return when(type.lowercase()){
        "email" -> { Patterns.EMAIL_ADDRESS.matcher(text).matches() }
        "password"->{ text.isNotEmpty() }
        "phone" -> { Patterns.PHONE.matcher(text).matches()}
        else -> { true }
    }
}

fun getKeyboard(userInfo: String): KeyboardType {
    return when (userInfo.lowercase()) {
        "phoneNumber" -> KeyboardType.Number
        "email" -> KeyboardType.Email
        else -> KeyboardType.Text
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

fun getSport():List<String>{

    return listOf(
        "Soccer",
        "Volleyball",
        "Basketball",
        "Cricket",
        "Baseball",
        "Football",
        "Golf",
        "Gymnastic",
        "Tennis"
        )

}

fun getLocation():List<String>{
    return listOf(
        "Turin",
        "Milan",
        "Rome",
        "Venice",
    )
}
fun getTimeSlot():List<String>{
    return listOf(
        "09:00-11:00",
        "11:00-13:00",
        "13:00-15:00",
        "15:00-17:00",
        "17:00-19:00",
        "19:00-21:00",
    )
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

const val QUALITY = 100

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