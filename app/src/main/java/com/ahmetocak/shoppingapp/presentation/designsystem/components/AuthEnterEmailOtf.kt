package com.ahmetocak.shoppingapp.presentation.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.utils.ComponentPreview

@Composable
fun AuthEnterEmailOtf(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    labelText: String
) {
    val focusManager = LocalFocusManager.current

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
            Icon(imageVector = Icons.Filled.Email, contentDescription = null)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        isError = isError,
        singleLine = true
    )
}

@ComponentPreview
@Composable
private fun AuthEnterEmailOtfPreview() {
    ShoppingAppTheme {
        Surface {
            AuthEnterEmailOtf(
                value = "",
                onValueChange = {},
                isError = false,
                labelText = stringResource(id = R.string.enter_email)
            )
        }
    }
}

@ComponentPreview
@Composable
private fun AuthEnterEmailOtfErrorPreview() {
    ShoppingAppTheme {
        Surface {
            AuthEnterEmailOtf(
                value = "",
                onValueChange = {},
                isError = true,
                labelText = stringResource(id = R.string.enter_valid_email)
            )
        }
    }
}