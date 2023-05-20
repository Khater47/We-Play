package com.example.mad.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.mad.R
import com.example.mad.common.getIconUserInfo
import com.example.mad.common.getKeyboard
import com.example.mad.ui.theme.MadTheme

@Composable
fun TextFieldDefault(text: String, setText: (String) -> Unit,placeholder:Int) {

    val isVisible = remember {
        mutableStateOf(false)
    }
    val placeholderText = stringResource(placeholder)

    OutlinedTextField(
        value = text,
        onValueChange = {
            setText(it)
        },
        label = {
            Text(text = placeholderText)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = getKeyboard(placeholderText),
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            if (isVisible.value) {
                IconButton(
                    onClick = { setText("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        }
    )

}

@Composable
fun TextFieldDefaultIcon(text: String, setText: (String) -> Unit,placeholder:Int) {

    val isVisible = remember {
        mutableStateOf(false)
    }

    val placeholderText = stringResource(placeholder)

    OutlinedTextField(
        value = text,
        onValueChange = {
            setText(it)
        },
        label = {
            Text(text = placeholderText)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = getKeyboard(placeholderText),
            imeAction = ImeAction.Done
        ),
        leadingIcon = { Icon(imageVector = getIconUserInfo(stringResource(placeholder)),
            contentDescription = "leadingIcon") },
        trailingIcon = {
            if (isVisible.value) {
                IconButton(
                    onClick = { setText("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        }
    )

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val (text,setText) = remember {
        mutableStateOf("")
    }
    MadTheme {
        TextFieldDefaultIcon(text,setText,R.string.email)
    }
}