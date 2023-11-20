package com.ahmetocak.shoppingapp.presentation.payment.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingButton

@Composable
fun PaymentButton(modifier: Modifier, onPaymentClicked: () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        ShoppingButton(
            onClick = onPaymentClicked,
            buttonText = stringResource(id = R.string.payment)
        )
    }
}