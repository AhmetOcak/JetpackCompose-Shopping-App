package com.ahmetocak.shoppingapp.presentation.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R

@Composable
fun RegisterNow(modifier: Modifier, onRegisterClick: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.no_account))
        Spacer(modifier = modifier.width(4.dp))
        TextButton(
            modifier = modifier,
            onClick = onRegisterClick,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = stringResource(id = R.string.register_now), fontWeight = FontWeight.Bold)
        }
    }
}