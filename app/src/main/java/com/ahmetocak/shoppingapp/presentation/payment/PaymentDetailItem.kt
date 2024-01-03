package com.ahmetocak.shoppingapp.presentation.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmetocak.shoppingapp.R

@Composable
fun RowScope.PaymentDetailItem(titleId: Int, description: Double) {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.two_level_margin)),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "$${String.format("%.2f", description)}", fontWeight = FontWeight.Bold)
            Text(text = stringResource(id = titleId), fontWeight = FontWeight.Light)
        }
    }
}