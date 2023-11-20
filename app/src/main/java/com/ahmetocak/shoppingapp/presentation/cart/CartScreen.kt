package com.ahmetocak.shoppingapp.presentation.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import com.ahmetocak.shoppingapp.presentation.cart.components.CartList
import com.ahmetocak.shoppingapp.presentation.cart.components.CheckOutButton
import com.ahmetocak.shoppingapp.presentation.cart.components.CheckoutDetails
import com.ahmetocak.shoppingapp.presentation.cart.components.EmptyCartListView

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onNavigatePaymentScreen: (Float) -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.consumedErrorMessage()
    }

    CartScreenContent(
        modifier = modifier,
        cartList = uiState.cartList,
        onRemoveItemClick = viewModel::removeProductFromCart,
        subtotal = uiState.subtotal,
        onIncreaseClicked = viewModel::increaseProductCount,
        onDecreaseClicked = viewModel::decreaseProductCount,
        onCheckoutBtnClicked = { onNavigatePaymentScreen((uiState.subtotal).toFloat()) }
    )
}

@Composable
private fun CartScreenContent(
    modifier: Modifier,
    cartList: List<CartEntity>,
    onRemoveItemClick: (Int) -> Unit,
    subtotal: Double,
    onIncreaseClicked: (Int) -> Unit,
    onDecreaseClicked: (Int) -> Unit,
    onCheckoutBtnClicked: () -> Unit
) {
    if (cartList.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = dimensionResource(id = R.dimen.two_level_margin))
        ) {
            CartList(
                modifier = modifier.weight(4f),
                cartList = cartList,
                onRemoveItemClick = onRemoveItemClick,
                onDecreaseClicked = onDecreaseClicked,
                onIncreaseClicked = onIncreaseClicked
            )
            CheckoutDetails(
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
                subtotal = subtotal
            )
            CheckOutButton(
                modifier = modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
                subtotal = subtotal,
                onCheckoutBtnClicked = onCheckoutBtnClicked
            )
        }
    } else {
        EmptyCartListView(modifier = modifier, messageId = R.string.cart_empty)
    }
}