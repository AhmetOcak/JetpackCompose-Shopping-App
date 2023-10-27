package com.ahmetocak.shoppingapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ahmetocak.shoppingapp.R

@Composable
fun ShoppingButton(modifier: Modifier = Modifier, onClick: () -> Unit, buttonText: String) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)),
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_corner_size))
    ) {
        Text(text = buttonText, style = MaterialTheme.typography.titleMedium)
    }
}