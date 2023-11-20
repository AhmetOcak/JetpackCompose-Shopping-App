package com.ahmetocak.shoppingapp.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ahmetocak.shoppingapp.R

@Composable
fun PageHeader(modifier: Modifier, onShoppingCartClicked: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier.padding(
                start = dimensionResource(id = R.dimen.two_level_margin),
                top = dimensionResource(id = R.dimen.two_level_margin)
            ),
            text = stringResource(id = R.string.discover_products),
            style = MaterialTheme.typography.headlineMedium
        )
        IconButton(onClick = onShoppingCartClicked) {
            Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = null)
        }
    }
}