package com.example.mad.common.composable


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.example.mad.MainViewModel
import com.example.mad.R
import com.example.mad.activity.BottomBarScreen
import com.example.mad.ui.theme.MadTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBasic(id: Int) {

    TopAppBar(
        title = {
            TextTopBar(id)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarAction(
    id: Int,
    icon: ImageVector,
    action: () -> Unit
) {

    TopAppBar(
        title = {
            TextTopBar(id)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = {
                action()
            }) {
                Icon(icon, "actionIcon", tint = Color.White)
            }

        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarActionBadge(
    id: Int,
    icon: ImageVector,
    action: () -> Unit,
    counter: Int,
) {

    TopAppBar(
        title = {
            TextTopBar(id)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            if(counter > 0){
                IconButton(onClick = {
                    action()
                }) {
                    BadgedBox(badge = {
                        Badge(){
                            Text(text = counter.toString())
                        }
                    }){
                        Icon(icon, "actionIcon", tint = Color.White)
                    }
                }
            }else{
                IconButton(onClick = {
                    action()
                }) {
                    Icon(icon, "actionIcon", tint = Color.White)
                }
            }

        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarProfile(
    vm:MainViewModel,
    navController: NavHostController,
) {

//    val context = LocalContext.current

    val expanded = remember {
        mutableStateOf(false)
    }

    TopAppBar(title = { TextTopBar(R.string.topBarUserProfile) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = {
                expanded.value = true
            }) {
                Icon(Icons.Default.MoreVert, "actionIcon", tint = Color.White)
            }

            DropdownMenu(
                modifier = Modifier.width(width = 150.dp),
                expanded = expanded.value,
                onDismissRequest = {
                    expanded.value = false
                },
                offset = DpOffset(x=(-102).dp,y=(-64).dp),
                properties = PopupProperties()
            ) {
                DropdownMenuItem(text = { Text("Edit Profile") }, onClick = {
                    navController.navigate(BottomBarScreen.ProfileEdit.route)
                })
                DropdownMenuItem(text = { Text("Logout") }, onClick = {
                    vm.logOut()
                })
            }
        }
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBackButton(id: Int, backAction: () -> Unit) {

    TopAppBar(
        title = {
            TextTopBar(id)
        },
        navigationIcon = {
            NavigationIconButtonTopBar(backAction)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComplete(
    id: Int,
    icon: ImageVector,
    backAction: () -> Unit,
    action: () -> Unit
) {

    TopAppBar(
        title = {
            TextTopBar(id)
        },
        navigationIcon = {
            NavigationIconButtonTopBar(backAction)
        },
        actions = {
            ActionButtonTopBar(icon, action)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreviewTopBarBack() {
    fun backAction() {}
    fun action() {}

    MadTheme {
        Column {
            TopBarBasic(R.string.topBarHome)
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            TopBarBackButton(R.string.topBarHome, ::backAction)
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            TopBarComplete(R.string.topBarHome, Icons.Default.Check, ::backAction, ::action)

        }
    }
}

