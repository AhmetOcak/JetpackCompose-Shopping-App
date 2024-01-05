package com.ahmetocak.shoppingapp.presentation.designsystem.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShoppingShowToastMessage(message: String) {
    Toast.makeText(
        LocalContext.current,
        message,
        Toast.LENGTH_SHORT
    ).show()
}