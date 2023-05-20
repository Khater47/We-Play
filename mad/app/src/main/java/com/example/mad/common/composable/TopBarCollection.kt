package com.example.mad.common.composable


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mad.R
import com.example.mad.ui.theme.MadTheme


@Composable
fun TopBarBasic(id:Int) {

    TopAppBar(
        title = {
            TextTopBar(id)
        }
    )
}

@Composable
fun TopBarBackButton(id:Int,backAction: () -> Unit) {

    TopAppBar(
        title = {
            TextTopBar(id)
        },
        navigationIcon = {
            NavigationIconButtonTopBar(backAction)
        }
    )
}

@Composable
fun TopBarAction(id:Int,icon:ImageVector,action: () -> Unit) {

    TopAppBar(
        title = {
            TextTopBar(id)
        },
        actions = {
            ActionButtonTopBar(icon, action)
        }
    )
}


@Composable
fun TopBarComplete(
    id:Int,
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
        }
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreviewTopBarBack() {
    fun backAction(){}
    fun action(){}

    MadTheme {
        Column{
            TopBarBasic(R.string.topBarHome)
            Spacer(modifier = Modifier.padding(vertical=20.dp))
            TopBarBackButton(R.string.topBarHome,::backAction)
            Spacer(modifier = Modifier.padding(vertical=20.dp))
            TopBarComplete(R.string.topBarHome,Icons.Default.Check,::backAction,::action)

        }
    }
}

