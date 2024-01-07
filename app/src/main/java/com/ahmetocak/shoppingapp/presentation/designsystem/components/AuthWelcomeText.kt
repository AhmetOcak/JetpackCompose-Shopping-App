package com.ahmetocak.shoppingapp.presentation.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.utils.ComponentPreview

@Composable
fun WelcomeText(modifier: Modifier, text: String) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.one_level_margin)),
        text = text,
        style = MaterialTheme.typography.headlineLarge
    )
}

@ComponentPreview
@Composable
private fun WelcomeTextPreview() {
    ShoppingAppTheme {
        Surface {
            WelcomeText(modifier = Modifier, text = "LOGIN NOW!")
        }
    }
}