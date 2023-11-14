package com.ahmetocak.shoppingapp.presentation.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.model.shopping.CreditCard
import com.ahmetocak.shoppingapp.ui.components.CreditCard
import com.ahmetocak.shoppingapp.ui.components.ShoppingButton
import com.ahmetocak.shoppingapp.utils.DELIVERY_FEE

@Composable
fun PaymentScreen(modifier: Modifier = Modifier) {

    val viewModel: PaymentViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    PaymentScreenContent(
        modifier = modifier,
        cardInfo = CreditCard(
            holderName = viewModel.holderName,
            number = viewModel.cardNumber,
            expiryDate = viewModel.expiryDate,
            cvc = viewModel.cvc
        ),
        onHolderNameChanged = {
            viewModel.updateHolderName(it)
        },
        onCardNumberChanged = {
            viewModel.updateCardNumber(it)
        },
        onCvcChanged = {
            viewModel.updateCVC(it)
        },
        onExpiryDateChanged = {
            viewModel.updateExpiryDate(it)
        },
        totalAmount = uiState.totalAmount + DELIVERY_FEE
    )
}

@Composable
private fun PaymentScreenContent(
    modifier: Modifier,
    cardInfo: CreditCard,
    onHolderNameChanged: (String) -> Unit,
    onCardNumberChanged: (String) -> Unit,
    onExpiryDateChanged: (String) -> Unit,
    onCvcChanged: (String) -> Unit,
    totalAmount: Double
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.two_level_margin))
    ) {
        CreditCard(cardInfo = cardInfo)
        CardDetails(
            modifier = modifier,
            onHolderNameChanged = onHolderNameChanged,
            onCardNumberChanged = onCardNumberChanged,
            onCvcChanged = onCvcChanged,
            onExpiryDateChanged = onExpiryDateChanged,
            cardInfo = cardInfo
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.four_level_margin))
                .height(96.dp),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.four_level_margin))
        ) {
            PaymentDetail(modifier = modifier, titleId = R.string.delivery, description = DELIVERY_FEE)
            PaymentDetail(modifier = modifier, titleId = R.string.total, description = totalAmount)
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
            Text(text = "$${String.format("%.2f", description)}", fontWeight = FontWeight.Bold)
            Text(text = stringResource(id = titleId), fontWeight = FontWeight.Light)
        }
    }
}

@Composable
private fun CardDetails(
    modifier: Modifier,
    onHolderNameChanged: (String) -> Unit,
    onCardNumberChanged: (String) -> Unit,
    onCvcChanged: (String) -> Unit,
    onExpiryDateChanged: (String) -> Unit,
    cardInfo: CreditCard
) {
    CardHolderName(
        modifier = modifier,
        onHolderNameChanged = onHolderNameChanged,
        holderNameVal = cardInfo.holderName
    )
    CardNumber(
        modifier = modifier,
        onCardNumberChanged = onCardNumberChanged,
        cardNumberVal = cardInfo.number
    )
    CardDateAndCVC(
        modifier = modifier,
        onExpiryDateChanged = onExpiryDateChanged,
        onCvcChanged = onCvcChanged,
        expiryDateVal = cardInfo.expiryDate,
        cvcVal = cardInfo.cvc
    )
}

@Composable
fun CardHolderName(
    modifier: Modifier,
    onHolderNameChanged: (String) -> Unit,
    holderNameVal: String
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        value = holderNameVal,
        onValueChange = onHolderNameChanged,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        label = {
            Text(text = stringResource(id = R.string.card_holder))
        }
    )
}

@Composable
private fun CardNumber(
    modifier: Modifier,
    onCardNumberChanged: (String) -> Unit,
    cardNumberVal: String
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        value = cardNumberVal,
        onValueChange = onCardNumberChanged,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text(text = stringResource(id = R.string.card_num))
        }
    )
}

@Composable
private fun CardDateAndCVC(
    modifier: Modifier,
    onCvcChanged: (String) -> Unit,
    onExpiryDateChanged: (String) -> Unit,
    cvcVal: String,
    expiryDateVal: String
) {
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
            value = expiryDateVal,
            onValueChange = onExpiryDateChanged,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(text = stringResource(id = R.string.expiry_date))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.expiry_date_placeholder))
            }
        )
        Spacer(modifier = modifier.width(32.dp))
        OutlinedTextField(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth(),
            value = cvcVal,
            onValueChange = onCvcChanged,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(text = stringResource(id = R.string.cvc))
            }
        )
    }
}