package com.ahmetocak.shoppingapp.presentation.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.designsystem.components.FullScreenCircularLoading
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingButton
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingCreditCard
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.model.shopping.CreditCard
import com.ahmetocak.shoppingapp.utils.DELIVERY_FEE

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    onContinueShoppingClick: () -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.consumedErrorMessage()
    }

    PaymentScreenContent(
        modifier = modifier,
        cardInfo = CreditCard(
            holderName = viewModel.holderName,
            number = viewModel.cardNumber,
            expiryDate = viewModel.expiryDate,
            cvc = viewModel.cvc
        ),
        onHolderNameChanged = viewModel::updateHolderName,
        onCardNumberChanged = viewModel::updateCardNumber,
        onCvcChanged = viewModel::updateCVC,
        onExpiryDateChanged = viewModel::updateExpiryDate,
        totalAmount = uiState.totalAmount + DELIVERY_FEE,
        onCardInputClicked = viewModel::updateRotateCard,
        rotated = viewModel.rotateCard,
        onCardClick = { viewModel.updateRotateCard(!viewModel.rotateCard) },
        onPaymentClicked = viewModel::payment,
        isPaymentDone = uiState.isPaymentDone,
        isLoading = uiState.isLoading,
        onContinueShoppingClick = onContinueShoppingClick
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
    totalAmount: Double,
    onCardInputClicked: (Boolean) -> Unit,
    rotated: Boolean,
    onCardClick: () -> Unit,
    onPaymentClicked: () -> Unit,
    isPaymentDone: Boolean,
    isLoading: Boolean,
    onContinueShoppingClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.two_level_margin))
    ) {
        if (isLoading) {
            FullScreenCircularLoading()
        } else if (isPaymentDone) {
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
                        .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
                    text = stringResource(id = R.string.payment_success),
                    textAlign = TextAlign.Center
                )
                ShoppingButton(
                    modifier = modifier.padding(top = dimensionResource(id = R.dimen.two_level_margin)),
                    onClick = onContinueShoppingClick,
                    buttonText = stringResource(id = R.string.continue_shopping)
                )
            }
        } else {
            ShoppingCreditCard(cardInfo = cardInfo, rotated = rotated, onCardClick = onCardClick)
            CardDetails(
                onHolderNameChanged = onHolderNameChanged,
                onCardNumberChanged = onCardNumberChanged,
                onCvcChanged = onCvcChanged,
                onExpiryDateChanged = onExpiryDateChanged,
                cardInfo = cardInfo,
                onCardInputClicked = onCardInputClicked
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.four_level_margin))
                    .height(96.dp),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.four_level_margin))
            ) {
                PaymentDetailItem(
                    titleId = R.string.delivery,
                    description = DELIVERY_FEE
                )
                PaymentDetailItem(
                    titleId = R.string.total,
                    description = totalAmount
                )
            }
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                ShoppingButton(
                    onClick = onPaymentClicked,
                    buttonText = stringResource(id = R.string.payment)
                )
            }
        }
    }
}

@Composable
fun CardDetails(
    onHolderNameChanged: (String) -> Unit,
    onCardNumberChanged: (String) -> Unit,
    onCvcChanged: (String) -> Unit,
    onExpiryDateChanged: (String) -> Unit,
    cardInfo: CreditCard,
    onCardInputClicked: (Boolean) -> Unit
) {
    CardHolderName(
        onHolderNameChanged = onHolderNameChanged,
        holderNameVal = cardInfo.holderName,
        onCardInputClicked = onCardInputClicked
    )
    CardNumber(
        onCardNumberChanged = onCardNumberChanged,
        cardNumberVal = cardInfo.number,
        onCardInputClicked = onCardInputClicked
    )
    CardDateAndCVC(
        onExpiryDateChanged = onExpiryDateChanged,
        onCvcChanged = onCvcChanged,
        expiryDateVal = cardInfo.expiryDate,
        cvcVal = cardInfo.cvc,
        onCardInputClicked = onCardInputClicked
    )
}

@Composable
private fun CardHolderName(
    onHolderNameChanged: (String) -> Unit,
    holderNameVal: String,
    onCardInputClicked: (Boolean) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
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
    onCardNumberChanged: (String) -> Unit,
    cardNumberVal: String,
    onCardInputClicked: (Boolean) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
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
    onCvcChanged: (String) -> Unit,
    onExpiryDateChanged: (String) -> Unit,
    cvcVal: String,
    expiryDateVal: String,
    onCardInputClicked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
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
        Spacer(modifier = Modifier.width(32.dp))
        OutlinedTextField(
            modifier = Modifier
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