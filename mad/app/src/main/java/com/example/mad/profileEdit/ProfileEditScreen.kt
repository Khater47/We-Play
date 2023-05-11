package com.example.mad.profileEdit


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.mad.R
import com.example.mad.UserViewModel
import com.example.mad.activity.BottomBarScreen
import com.example.mad.model.Profile
import com.example.mad.utils.getIconUserInfo
import com.example.mad.utils.getImageFromInternalStorage
import com.example.mad.utils.getKeyboard
import com.example.mad.utils.invalidField
import com.example.mad.utils.openCamera
import com.example.mad.utils.openGallery
import com.example.mad.utils.rotateBitmap
import com.example.mad.utils.saveImageBitmapOnInternalStorage
import com.example.mad.utils.saveImageUriOnInternalStorage
import com.example.mad.utils.uriToBitmap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

const val CAMERA = android.Manifest.permission.CAMERA
const val READ_EXT_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val READ_MEDIA_IMAGES = android.Manifest.permission.READ_MEDIA_IMAGES

/*
TODO():
    fix rotate camera intent

*/



@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ProfileEditScreen(vm: UserViewModel, navController: NavHostController, userId: String?) {

    val configuration = LocalConfiguration.current

    val userObject = rememberSaveable {
        mutableStateOf(Bundle())
    }


    Scaffold(
        topBar = { TopAppBarEditProfile(userObject, navController, vm, userId) }
    ) {
        Box(Modifier.padding(it)) {

            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitEditProfile(userObject)
                }

                else -> {
                    LandscapeEditProfile(userObject)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PortraitEditProfile(userObject: MutableState<Bundle>) {

    Column(
        Modifier
            .fillMaxWidth(),
    ) {
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) { EditImageProfile() }

        Spacer(Modifier.padding(top = 40.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .padding(16.dp)
        ) { EditUserInfo(userObject) }


    }

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LandscapeEditProfile(userObject: MutableState<Bundle>) {

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
            EditUserInfo(userObject)
        }


    }
}


@Composable
fun TopAppBarEditProfile(
    userObject: MutableState<Bundle>,
    navController: NavHostController,
    vm: UserViewModel,
    userId: String?
) {

    val context = LocalContext.current


    TopAppBar(
        title = {

            Text(
                text = "Edit Profile",
                color = Color.White,
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(BottomBarScreen.Profile.route)
            }) {
                Icon(Icons.Default.ArrowBack, "arrowBack", Modifier.size(28.dp))
            }
        },
        actions = {
            IconButton(onClick = {

                if (invalidField(userObject.value)) {
                    Toast.makeText(
                        context,
                        "Fill all fields with correct value",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val p = Profile(
                        userId?.toInt() ?: 0,
                        userObject.value.getString("FullName") ?: "",
                        userObject.value.getString("Email") ?: "",
                        userObject.value.getString("Nickname") ?: "",
                        userObject.value.getString("Description") ?: "",
                        userObject.value.getString("PhoneNumber") ?: ""
                    )
                    vm.insertProfile(p)
                    navController.navigate(BottomBarScreen.Profile.route)
                }


            }) {
                Icon(Icons.Default.Check, "Edit", Modifier.size(28.dp))
            }
        }

    )


}


@Composable
fun DialogPermission(text: String) {

    val context = LocalContext.current

    var showRational by remember {
        mutableStateOf(true)
    }

    //Dialog for handle denied permission
    if (showRational) {

        AlertDialog(onDismissRequest = { showRational = false },
            title = { Text("Permission Dialog", fontSize = 24.sp) },
            confirmButton = {
                Button(onClick = {
                    showRational = false
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                    )
                }, modifier = Modifier.clip(RectangleShape)) {
                    Text("GRANT PERMISSION")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showRational = false },
                    Modifier.clip(RectangleShape)
                ) {
                    Text("CANCEL")
                }
            },
            text = {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
            }
        )
    }
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

                if(result.data?.data!=null){
                    imageUri = result.data?.data as Uri
                    loadImage = saveImageUriOnInternalStorage(imageUri,context)
                }
                else  loadImage=BitmapFactory.decodeResource(context.resources,R.drawable.profile)



            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            result -> if (result.resultCode == Activity.RESULT_OK) {

            val inputImage = uriToBitmap(imageUri,context)
            val rotated = rotateBitmap(inputImage,imageUri,context)
            saveImageUriOnInternalStorage(imageUri,context)
            loadImage = rotated ?:
                BitmapFactory.decodeResource(context.resources,R.drawable.profile)
        }


        }
    )

    //handle permission
    MultiplePermissions()

    Spacer(modifier = Modifier.padding(vertical = 10.dp))

    Box(
        Modifier.height(150.dp)
    ) {

        Image(
            bitmap = loadImage.asImageBitmap(),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )
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
                                openGallery(context,imagePicker)
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
//                                cameraLauncher.launch(null)
                                imageUri=openCamera(context,cameraLauncher) as Uri

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


@Composable
fun EditUserInfo(userObject: MutableState<Bundle>) {

    val userInfo = listOf(
        "FullName",
        "Email",
        "Nickname",
        "PhoneNumber",
        "Description"
    )

    LazyColumn {
        items(userInfo, itemContent = { item ->
            EditInfo(item, getIconUserInfo(item), userObject)
        })
    }


}


@Composable
fun EditInfo(text: String, icon: ImageVector, userObject: MutableState<Bundle>) {

    val info = remember { mutableStateOf(userObject.value.getString(text) ?: "") }

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
                userObject.value.putString(text, it)
            },
            label = { Text(text = text) },
            keyboardOptions = KeyboardOptions(
                keyboardType = getKeyboard(text),
                imeAction = ImeAction.Done
            ),

            )
    }
}


//Portrait Preview

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        ProfileEditScreen()
//    }
//}

//Landscape Preview

//@Preview(showBackground = true,showSystemUi = true, device="spec:width=411dp,height=891dp,orientation=landscape")
//@Composable
//fun DefaultPreview() {
//    MadTheme {
//        ProfileEditScreen()
//    }
//}