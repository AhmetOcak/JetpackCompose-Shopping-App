package com.ahmetocak.shoppingapp.presentation.product.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R

@Composable
fun AddToCartRow(
    modifier: Modifier,
    price: Double,
    onAddToCartClicked: () -> Unit,
    cartButtonText: String
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.two_level_margin)),
            text = "$$price",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.four_level_margin),
                    end = dimensionResource(id = R.dimen.two_level_margin)
                ),
            onClick = onAddToCartClicked,
            contentPadding = PaddingValues(vertical = 12.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = cartButtonText,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}