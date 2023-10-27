package com.ahmetocak.shoppingapp.presentation.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.ui.components.ShoppingButton

@Composable
fun PaymentScreen(modifier: Modifier = Modifier) {

    PaymentScreenContent(modifier = modifier)
}

@Composable
private fun PaymentScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.two_level_margin))
    ) {
        CreditCartImage(modifier = modifier)
        CardDetails(modifier = modifier)
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.four_level_margin))
                .height(96.dp),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.four_level_margin))
        ) {
            PaymentDetail(modifier = modifier, titleId = R.string.delivery, description = 5.00)
            PaymentDetail(modifier = modifier, titleId = R.string.total, description = 1370.00)
        }
        PaymentButton(modifier = modifier)
    }
}

@Composable
private fun PaymentButton(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        ShoppingButton(onClick = { /*TODO*/ }, buttonText = stringResource(id = R.string.payment))
    }
}

@Composable
private fun RowScope.PaymentDetail(modifier: Modifier, titleId: Int, description: Double) {
    Card(
        modifier = modifier
            .weight(1f)
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.two_level_margin)),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "$$description", fontWeight = FontWeight.Bold)
            Text(text = stringResource(id = titleId), fontWeight = FontWeight.Light)
        }
    }
}

@Composable
private fun CreditCartImage(modifier: Modifier) {
    Image(
        modifier = modifier.fillMaxWidth(),
        painter = painterResource(id = R.drawable.card),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun CardDetails(modifier: Modifier) {
    CardNumber(modifier = modifier)
    CardDateAndCVC(modifier = modifier)
}

// TODO max 16 karakter ve 4 karakterde bir boşluk bırakma işi yapılacak
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CardNumber(modifier: Modifier) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        value = "",
        onValueChange = {},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text(text = stringResource(id = R.string.card_num))
        }
    )
}

// TODO karakter ayarı yapılacak
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardDateAndCVC(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth(),
            value = "",
            onValueChange = {},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(text = stringResource(id = R.string.expiry_date))
            }
        )
        Spacer(modifier = modifier.width(32.dp))
        OutlinedTextField(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth(),
            value = "",
            onValueChange = {},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(text = stringResource(id = R.string.cvc))
            }
        )
    }
}