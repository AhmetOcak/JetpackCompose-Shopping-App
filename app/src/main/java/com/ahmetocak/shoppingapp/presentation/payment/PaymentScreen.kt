package com.ahmetocak.shoppingapp.presentation.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.designsystem.components.FullScreenCircularLoading
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingButton
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingCreditCard
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.model.shopping.CreditCard
import com.ahmetocak.shoppingapp.presentation.payment.components.CardDetails
import com.ahmetocak.shoppingapp.presentation.payment.components.PaymentDetail
import com.ahmetocak.shoppingapp.presentation.payment.components.PaymentSuccessView
import com.ahmetocak.shoppingapp.utils.DELIVERY_FEE

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    onNavigateHomeScreen: () -> Unit,
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
        onContinueShoppingClick = { onNavigateHomeScreen() }
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
            PaymentSuccessView(
                modifier = modifier,
                onContinueShoppingClick = onContinueShoppingClick
            )
        } else {
            ShoppingCreditCard(cardInfo = cardInfo, rotated = rotated, onCardClick = onCardClick)
            CardDetails(
                modifier = modifier,
                onHolderNameChanged = onHolderNameChanged,
                onCardNumberChanged = onCardNumberChanged,
                onCvcChanged = onCvcChanged,
                onExpiryDateChanged = onExpiryDateChanged,
                cardInfo = cardInfo,
                onCardInputClicked = onCardInputClicked
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.four_level_margin))
                    .height(96.dp),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.four_level_margin))
            ) {
                PaymentDetail(
                    modifier = modifier,
                    titleId = R.string.delivery,
                    description = DELIVERY_FEE
                )
                PaymentDetail(
                    modifier = modifier,
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