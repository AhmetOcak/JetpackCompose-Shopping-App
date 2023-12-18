package com.ahmetocak.shoppingapp.presentation.cart.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.utils.DELIVERY_FEE

@Composable
fun CheckOutButton(
    modifier: Modifier,
    subtotal: Double,
    onCheckoutBtnClicked: () -> Unit
) {
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.two_level_margin))
    )
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onCheckoutBtnClicked,
        contentPadding = PaddingValues(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.checkout))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" $${String.format("%.2f", subtotal + DELIVERY_FEE)}")
                }
            },
            style = MaterialTheme.typography.titleMedium
        )
    }
}