package com.ahmetocak.shoppingapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ahmetocak.shoppingapp.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AuthEnterEmailOtf(modifier: Modifier, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.one_level_margin)),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(id = R.string.enter_email))
        },
        trailingIcon = {
            Icon(imageVector = Icons.Filled.Email, contentDescription = null)
        }
    )
}