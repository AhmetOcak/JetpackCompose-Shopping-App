package com.ahmetocak.shoppingapp.presentation.payment.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingButton

@Composable
fun PaymentSuccessView(modifier: Modifier, onContinueShoppingClick: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
            painter = painterResource(id = R.drawable.payment_success),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin))
                .padding(
                    top = dimensionResource(id = R.dimen.two_level_margin)
                ),
            text = stringResource(id = R.string.payment_success),
            textAlign = TextAlign.Center
        )
        ShoppingButton(
            modifier = modifier.padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            onClick = onContinueShoppingClick,
            buttonText = stringResource(id = R.string.continue_shopping)
        )
    }
}