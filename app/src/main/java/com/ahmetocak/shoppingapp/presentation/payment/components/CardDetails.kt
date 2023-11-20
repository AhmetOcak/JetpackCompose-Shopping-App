package com.ahmetocak.shoppingapp.presentation.payment.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.model.shopping.CreditCard

@Composable
fun CardDetails(
    modifier: Modifier,
    onHolderNameChanged: (String) -> Unit,
    onCardNumberChanged: (String) -> Unit,
    onCvcChanged: (String) -> Unit,
    onExpiryDateChanged: (String) -> Unit,
    cardInfo: CreditCard,
    onCardInputClicked: (Boolean) -> Unit
) {
    CardHolderName(
        modifier = modifier,
        onHolderNameChanged = onHolderNameChanged,
        holderNameVal = cardInfo.holderName,
        onCardInputClicked = onCardInputClicked
    )
    CardNumber(
        modifier = modifier,
        onCardNumberChanged = onCardNumberChanged,
        cardNumberVal = cardInfo.number,
        onCardInputClicked = onCardInputClicked
    )
    CardDateAndCVC(
        modifier = modifier,
        onExpiryDateChanged = onExpiryDateChanged,
        onCvcChanged = onCvcChanged,
        expiryDateVal = cardInfo.expiryDate,
        cvcVal = cardInfo.cvc,
        onCardInputClicked = onCardInputClicked
    )
}

@Composable
fun CardHolderName(
    modifier: Modifier,
    onHolderNameChanged: (String) -> Unit,
    holderNameVal: String,
    onCardInputClicked: (Boolean) -> Unit
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
        },
        interactionSource = interactionSource(
            cardRotate = false,
            onCardInputClicked = onCardInputClicked
        )
    )
}

@Composable
private fun CardNumber(
    modifier: Modifier,
    onCardNumberChanged: (String) -> Unit,
    cardNumberVal: String,
    onCardInputClicked: (Boolean) -> Unit
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
        },
        interactionSource = interactionSource(
            cardRotate = false,
            onCardInputClicked = onCardInputClicked
        )
    )
}

@Composable
private fun CardDateAndCVC(
    modifier: Modifier,
    onCvcChanged: (String) -> Unit,
    onExpiryDateChanged: (String) -> Unit,
    cvcVal: String,
    expiryDateVal: String,
    onCardInputClicked: (Boolean) -> Unit
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
            },
            interactionSource = interactionSource(
                cardRotate = false,
                onCardInputClicked = onCardInputClicked
            )
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
            },
            interactionSource = interactionSource(
                cardRotate = true,
                onCardInputClicked = onCardInputClicked
            )
        )
    }
}

@Composable
private fun interactionSource(
    cardRotate: Boolean,
    onCardInputClicked: (Boolean) -> Unit
): MutableInteractionSource {
    return remember { MutableInteractionSource() }
        .also { interactionSource ->
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        onCardInputClicked(cardRotate)
                    }
                }
            }
        }
}