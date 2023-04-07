package com.example.mad

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
//import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream

const val QUALITY = 100

//convert Uri to Bitmap
fun uriToBitmap(selectedFileUri:Uri?,context: Context):Bitmap?{
    try{
        val parcelFileDescriptor =
            selectedFileUri?.let { context.contentResolver.openFileDescriptor(it,"r") }

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
fun rotateBitmap(input: Bitmap?,imageUri:Uri?,context: Context): Bitmap? {
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
fun saveImageOnInternalStorage(imageUri:Uri?, context: Context){

    val directory = context.filesDir
    val imageFile = File(directory, context.resources.getString(R.string.imageName))

    val bitmap = if(imageUri!=null) //select picture
    {
        val tempBitmap  = uriToBitmap(imageUri,context)
        rotateBitmap(tempBitmap,imageUri,context)
        //val imageSource = ImageDecoder.createSource(context.contentResolver,imageUri as Uri)
        //ImageDecoder.decodeBitmap(imageSource)
    }

    else if(imageUri==null && imageFile!=null) //set previous image, if user don't select picture
        BitmapFactory.decodeFile(imageFile.absolutePath)

    else  //no image provided, set default image
        BitmapFactory.decodeResource(context.resources,R.drawable.profile)


    val outputStream = FileOutputStream(imageFile)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream)
    outputStream.flush()
    outputStream.close()
}

fun getImageFromInternalStorage(context: Context): Bitmap {

    val fileName = context.resources.getString(R.string.imageName)
    val directory = context.filesDir

    val imageFile = File(directory, fileName)

    return if (imageFile.totalSpace > 0)

        //get selected image
        BitmapFactory.decodeFile(imageFile.absolutePath)

    //get default image
    else
        BitmapFactory.decodeResource(context.resources, R.drawable.profile)

}