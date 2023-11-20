package com.ahmetocak.shoppingapp.presentation.profile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.ahmetocak.shoppingapp.R

@Composable
fun UserName(userName: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.six_level_margin))
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        text = userName,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        color = Color.Black
    )
}