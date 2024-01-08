package com.ahmetocak.shoppingapp.presentation.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.utils.ComponentPreview

@Composable
fun AuthEnterPasswordOtf(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean ,
    labelText: String
) {
    var visibility by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.one_level_margin)),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = labelText)
        },
        trailingIcon = {
            IconButton(onClick = { visibility = !visibility }) {
                Icon(
                    painter = painterResource(
                        id = if (visibility) {
                            R.drawable.ic_visibility
                        } else {
                            R.drawable.ic_visibility_off
                        }
                    ),
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (visibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        isError = isError
    )
}

@ComponentPreview
@Composable
private fun AuthEnterPasswordOtfPreview() {
    ShoppingAppTheme {
        Surface {
            AuthEnterEmailOtf(
                value = "",
                onValueChange = {},
                isError = false,
                labelText = stringResource(id = R.string.enter_password)
            )
        }
    }
}

@ComponentPreview
@Composable
private fun AuthEnterPasswordOtfErrorPreview() {
    ShoppingAppTheme {
        Surface {
            AuthEnterEmailOtf(
                value = "",
                onValueChange = {},
                isError = true,
                labelText = stringResource(id = R.string.pass_length)
            )
        }
    }
}