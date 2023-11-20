package com.ahmetocak.shoppingapp.presentation.login.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ahmetocak.shoppingapp.R

@Composable
fun ForgotPasswordButton(onForgotPasswordClick: () -> Unit) {
    TextButton(onClick = onForgotPasswordClick) {
        Text(
            text = stringResource(id = R.string.forgot_password),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}