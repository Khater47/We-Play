package com.example.mad.common.composable


import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.mad.R
import com.example.mad.common.getIconUserInfo
import com.example.mad.common.getKeyboard
import com.example.mad.ui.theme.MadTheme

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

@Composable
fun PasswordTextField(text:String,setText: (String) -> Unit){
    val passwordVisible = remember {
        mutableStateOf(false)
    }

    val placeholderText = "Password"

    OutlinedTextField(
        value = text,
        onValueChange = {
            setText(it)
        },
        label = {
            Text(text = placeholderText)
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None
                                else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = { Icon(imageVector = getIconUserInfo(placeholderText),
            contentDescription = "leadingIcon") },
        singleLine = true,
        trailingIcon = {

        val image = if (passwordVisible.value)   Icons.Default.Visibility  else Icons.Default.VisibilityOff
        val description =        if (passwordVisible.value) "Hide password" else "Show password"
        IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
            Icon(image, description)    }

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