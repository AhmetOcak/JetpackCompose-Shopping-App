package com.ahmetocak.shoppingapp.presentation.profile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SignOutButton(onSignOutClicked: () -> Unit) {
    IconButton(onClick = onSignOutClicked) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = null,
            tint = Color.Black
        )
    }
}