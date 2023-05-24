package com.example.mad.screens.profile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.common.composable.CircleImage
import com.example.mad.common.composable.ConfirmAlertButton
import com.example.mad.common.composable.DismissAlertButton
import com.example.mad.common.composable.TextBasicHeadLine
import com.example.mad.common.composable.TextFieldDefault
import com.example.mad.common.composable.TopBarComplete
import com.example.mad.common.getIconUserInfo
import com.example.mad.common.getImageFromInternalStorage
import com.example.mad.common.getKeyboard
import com.example.mad.common.openCamera
import com.example.mad.common.openGallery
import com.example.mad.common.rotateBitmap
import com.example.mad.common.saveImageUriOnInternalStorage
import com.example.mad.common.uriToBitmap
import com.example.mad.model.Profile
import com.example.mad.ui.theme.MadTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

const val CAMERA = android.Manifest.permission.CAMERA
const val READ_EXT_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val READ_MEDIA_IMAGES = android.Manifest.permission.READ_MEDIA_IMAGES


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ProfileEditScreen(
navController: NavHostController,
vm:MainViewModel
) {

    val orientation = LocalConfiguration.current.orientation
    val route = BottomBarScreen.Profile.route
    val userId = "f9SYx0LJM3TSDxUFMcX6JEwcaxh1"

    val user = remember{
        mutableStateOf(
            mapOf(
                "FullName" to "",
                "Nickname" to "",
                "Email" to "",
                "PhoneNumber" to "",
                "Description" to ""
            )
        )
    }

    fun goToProfile() {
        navController.navigate(route)
    }

    fun saveProfile() {
        val p = Profile(
            description=user.value.getOrDefault("Description","student"),
            fullName=user.value.getOrDefault("FullName","Mario Rossi") ,
            nickname =user.value.getOrDefault("Nickname","mario") ,
            phone=user.value.getOrDefault("PhoneNumber","3456789871"),
            email=user.value.getOrDefault("Email","mariorossi@gmail.com")
        )
        Log.d("TAG",p.toString())
        vm.insertUserProfile(userId,p)
        navController.navigate(route)
    }

    Scaffold(
        topBar = {
            TopBarComplete(
                id = R.string.topBarEditProfile,
                icon = Icons.Default.Check,
                backAction = ::goToProfile,
                action = ::saveProfile
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitEditProfile(user)
                }

                else -> {
                    LandscapeEditProfile(user)
                }
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PortraitEditProfile(
    user: MutableState<Map<String,String>>

) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditImageProfile()
        }

        Column(
            Modifier
                .weight(2.8f)
                .padding(start = 20.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            EditUserInfo(user)
        }
    }
}

@Composable
fun EditUserInfo(
    user: MutableState<Map<String,String>>

) {
    val userInfo = listOf(
        "FullName",
        "Email",
        "Nickname",
        "PhoneNumber",
        "Description"
    )

    LazyColumn {
        items(userInfo, itemContent = { item ->
            EditInfo(item, getIconUserInfo(item) ,user)
        })
    }

}

@Composable
fun EditInfo(
    text: String,
    icon: ImageVector,
    user: MutableState<Map<String,String>>

) {



    val info = remember {
        mutableStateOf(user.value.getValue(text))
//        mutableStateOf("")
    }

    Row(
        Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            Modifier
                .padding(end = 10.dp)
                .size(28.dp)
        ) {
            Icon(
                icon,
                "iconTextField",
            )
        }

        OutlinedTextField(
            value = info.value,
            onValueChange = {
                info.value = it
                val p:Map<String,String> = mapOf(
                    "FullName" to if(text!="FullName") user.value.getValue("FullName") else it,
                    "Nickname" to if(text!="Nickname") user.value.getValue("Nickname") else it,
                    "Email" to if(text!="Email") user.value.getValue("Email") else it,
                    "PhoneNumber" to if(text!="PhoneNumber") user.value.getValue("PhoneNumber") else it,
                    "Description" to if(text!="Description") user.value.getValue("Description") else it,
                )
                user.value = p
//                userObject.value.putString(text, it)
            },
            label = { Text(text = text) },
            keyboardOptions = KeyboardOptions(
                keyboardType = getKeyboard(text),
                imeAction = ImeAction.Done
            ),

            )
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditImageProfile() {

    val context = LocalContext.current

    val cameraGranted = context.checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED

    //Menu for camera/gallery intent
    var showMenu by remember {
        mutableStateOf(false)
    }

    var loadImage by remember {
        mutableStateOf(getImageFromInternalStorage(context))
    }

    var imageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                if (result.data?.data != null) {
                    imageUri = result.data?.data as Uri
                    loadImage = saveImageUriOnInternalStorage(imageUri, context)
                } else loadImage =
                    BitmapFactory.decodeResource(context.resources, R.drawable.profile)


            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val inputImage = uriToBitmap(imageUri, context)
                val rotated = rotateBitmap(inputImage, imageUri, context)
                saveImageUriOnInternalStorage(imageUri, context)
                loadImage =
                    rotated ?: BitmapFactory.decodeResource(context.resources, R.drawable.profile)
            }


        }
    )

    //handle permission
    MultiplePermissions()

    Spacer(modifier = Modifier.padding(vertical = 10.dp))


    Box(
        Modifier.height(150.dp)
    ) {
        CircleImage(loadImage.asImageBitmap())
        Box(
            Modifier
                .width(60.dp)
                .padding(start = 20.dp)
                .height(40.dp)
                .clip(CircleShape)
                .background(Color(14, 36, 50))
                .align(Alignment.TopEnd),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    val galleryGranted = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        context.checkSelfPermission(READ_EXT_STORAGE) == PackageManager.PERMISSION_GRANTED
                    } else {
                        context.checkSelfPermission(READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    }

                    if (cameraGranted || galleryGranted)
                        showMenu = !showMenu
                    else
                        Toast.makeText(
                            context, "Go to app settings for activate " +
                                    "permission for camera and gallery", Toast.LENGTH_LONG
                        ).show()

                }) {
                    Icon(
                        Icons.Default.PhotoCamera, "cameraIconButton",
                        Modifier.size(25.dp),
                        tint = Color.White
                    )
                }

                DropdownMenu(expanded = showMenu,
                    onDismissRequest = { showMenu = false }) {

                    DropdownMenuItem(onClick = {

                        val galleryGranted = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            context.checkSelfPermission(READ_EXT_STORAGE) == PackageManager.PERMISSION_GRANTED
                        } else {
                            context.checkSelfPermission(READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                        }

                        if (galleryGranted) {
                            showMenu = false
                            openGallery(imagePicker)
                        } else {
                            Toast.makeText(
                                context, "Go to app settings for activate " +
                                        "permission for gallery", Toast.LENGTH_LONG
                            ).show()
                        }
                    }) {
                        Text("Select image from gallery")
                    }

                    DropdownMenuItem(onClick = {
                        if (cameraGranted) {

                            showMenu = false
                            imageUri = openCamera(context, cameraLauncher) as Uri

                        } else {
                            Toast.makeText(
                                context, "Go to app settings for activate " +
                                        "permission for camera", Toast.LENGTH_LONG
                            ).show()
                        }


                    }) {
                        Text("Take a picture")
                    }
                }

            }
        }
    }


}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LandscapeEditProfile(
    user: MutableState<Map<String,String>>

) {

    Row(
        Modifier
            .fillMaxWidth()
    ) {

        Column(
            Modifier
                .weight(1f)
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditImageProfile()
        }


        Column(
            Modifier
                .weight(2f)
                .padding(start = 20.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            EditUserInfo(user)
        }


    }
}


@Composable
fun DialogPermission(text: String) {

    val context = LocalContext.current

    var showRational by remember {
        mutableStateOf(true)
    }
    fun confirm(){
        showRational = false
        context.startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            )
        )
    }

    fun dismiss(){
        showRational = false
    }

    AlertDialog(onDismissRequest = { dismiss() },
        title = { Text("Permission Dialog", fontSize = 24.sp) },
        confirmButton = { ConfirmAlertButton(::confirm)},
        dismissButton = { DismissAlertButton(::dismiss)},
        text = { TextBasicHeadLine(text) }
    )

}


@ExperimentalPermissionsApi
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MultiplePermissions() {

    val permission = mutableListOf<String>()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        permission.addAll(
            listOf(
                READ_MEDIA_IMAGES,
                CAMERA,
            )
        )
    } else {
        permission.addAll(
            listOf(
                READ_EXT_STORAGE,
                CAMERA,
            )
        )
    }

    val permissionStates = rememberMultiplePermissionsState(
        permissions = permission
    )


    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionStates.launchMultiplePermissionRequest()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    val deniedPermission =
        permissionStates.permissions.map { it.status.shouldShowRationale }.contains(true)

    if (deniedPermission) {
        DialogPermission(
            text = "Camera and External storage permission " +
                    "is needed for change user profile image." +
                    "\nOtherwise you can use default image."
        )
    }

}


//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewProfileEdit() {
//    MadTheme {
//        ProfileEditScreen()
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreviewProfileEditLandscape() {
//    MadTheme {
//        ProfileEditScreen()
//    }
//}
