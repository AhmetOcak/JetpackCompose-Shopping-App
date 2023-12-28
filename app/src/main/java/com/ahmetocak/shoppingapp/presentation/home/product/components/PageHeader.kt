package com.ahmetocak.shoppingapp.presentation.home.product.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
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
fun PageHeader(onShoppingCartClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(
            start = dimensionResource(id = R.dimen.two_level_margin),
            top = dimensionResource(id = R.dimen.one_level_margin)
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.discover_products),
            style = MaterialTheme.typography.headlineMedium
        )
        IconButton(onClick = onShoppingCartClicked) {
            Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = null)
        }
    }
}