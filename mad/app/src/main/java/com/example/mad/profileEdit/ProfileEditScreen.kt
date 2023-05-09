package com.example.mad.profileEdit


import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.mad.utils.getIconUserInfo
import com.example.mad.utils.getImageFromInternalStorage
import com.example.mad.utils.getKeyboard
import com.example.mad.utils.saveImageBitmapOnInternalStorage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

const val CAMERA = android.Manifest.permission.CAMERA
const val READ_EXT_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ProfileEditScreen() {

    val configuration = LocalConfiguration.current

    Scaffold(
        topBar = { TopAppBarEditProfile() }
    ) {
        Box(Modifier.padding(it)) {

            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitEditProfile()
                }

                else -> {
                    LandscapeEditProfile()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun PortraitEditProfile() {

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
        ) { EditUserInfo() }


    }

}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun LandscapeEditProfile() {

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
            EditUserInfo()
        }


    }
}


@Composable
fun TopAppBarEditProfile() {

    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(
                text = "Edit Profile",
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(context, "Go Back", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.ArrowBack, "arrowBack", Modifier.size(28.dp))
            }
        },
        actions = {
            IconButton(onClick = {
                Toast.makeText(context, "Load Edit Profile", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.Edit, "Edit", Modifier.size(28.dp))
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
@Composable
fun MultiplePermissions() {

    val permissionStates = rememberMultiplePermissionsState(
        permissions = listOf(
            READ_EXT_STORAGE,
            CAMERA,
        )
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


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditImageProfile() {

    val context = LocalContext.current

    //Menu for camera/gallery intent
    var showMenu by remember {
        mutableStateOf(false)
    }

    var loadImage by remember{
        mutableStateOf(getImageFromInternalStorage(context))
    }

//    var hasImage by remember {
//        mutableStateOf(false)
//    }



//    var imageUri by remember {
//        mutableStateOf<Uri>(Uri.EMPTY)
//    }
//
//    val packageName = context.packageName
//    context.grantUriPermission(
//        packageName,
//        imageUri,
//        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
//    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            if(it!=null){
                saveImageBitmapOnInternalStorage(it,context)
                loadImage=it
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
            bitmap=loadImage.asImageBitmap(),
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
                    showMenu = !showMenu
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
                        if (context.checkSelfPermission(READ_EXT_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            //TODO(Intent read gallery)
                            showMenu = false

                        }
                    }) {
                        Text("Select image from gallery")
                    }

                    DropdownMenuItem(onClick = {
                        if (context.checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            //TODO(Intent take picture)
                            showMenu = false
                            cameraLauncher.launch(null)

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
fun EditUserInfo() {

    val userInfo = listOf(
        "FullName",
        "Email",
        "Nickname",
        "PhoneNumber",
        "Description"
    )

    LazyColumn {
        items(userInfo, itemContent = { item ->
            EditInfo(item, getIconUserInfo(item))
        })
    }


}

@Composable
fun EditInfo(text: String, icon: ImageVector) {

    val info = remember { mutableStateOf("") }


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