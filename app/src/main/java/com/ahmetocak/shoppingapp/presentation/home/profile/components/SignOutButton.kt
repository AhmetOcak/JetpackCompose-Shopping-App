package com.ahmetocak.shoppingapp.presentation.home.profile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SignOutButton(onSignOutClicked: () -> Unit) {
    IconButton(onClick = onSignOutClicked) {
        Icon(
            imageVector = Icons.Filled.ExitToApp,
            contentDescription = null,
            tint = Color.Black
        )
    }
}