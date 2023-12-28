package com.ahmetocak.shoppingapp.presentation.product_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.presentation.product_detail.components.AddToCartRow
import com.ahmetocak.shoppingapp.presentation.product_detail.components.ProductImage
import com.ahmetocak.shoppingapp.presentation.product_detail.components.ProductInfo

@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    onCartClick: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.userMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.userMessages.first().asString())
        viewModel.consumedUserMessages()
    }

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.consumedErrorMessages()
    }

    ProductDetailScreenContent(
        modifier = modifier,
        product = uiState.product,
        isProductFavorite = uiState.isProductFavorite,
        onFavoriteBtnClicked = viewModel::onFavoriteProductClick,
        onAddToCartClicked = remember {
            {
                if (uiState.isProductInCart) {
                    onCartClick()
                } else {
                    viewModel.addProductToCart()
                }
            }
        },
        cartButtonText = if (uiState.isProductInCart) {
            stringResource(id = R.string.go_to_cart)
        } else {
            stringResource(id = R.string.add_to_cart)
        }
    )
}

@Composable
private fun ProductDetailScreenContent(
    modifier: Modifier,
    product: Product?,
    isProductFavorite: Boolean,
    onFavoriteBtnClicked: () -> Unit,
    onAddToCartClicked: () -> Unit,
    cartButtonText: String
) {
    if (product != null) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            ProductImage(
                modifier = modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.nine_level_margin)),
                imgUrl = product.image ?: "null"
            )
            ProductDetails(
                modifier = modifier
                    .weight(1f)
                    .fillMaxSize(),
                title = product.title ?: "null",
                description = product.description ?: "null",
                price = product.price?.toDouble() ?: 0.0,
                rate = product.rating?.rate ?: 0.0,
                isProductFavorite = isProductFavorite,
                onFavoriteBtnClicked = onFavoriteBtnClicked,
                onAddToCartClicked = onAddToCartClicked,
                cartButtonText = cartButtonText
            )
        }
    }
}

@Composable
private fun ProductDetails(
    modifier: Modifier,
    title: String,
    description: String,
    price: Double,
    rate: Double,
    onFavoriteBtnClicked: () -> Unit,
    isProductFavorite: Boolean,
    onAddToCartClicked: () -> Unit,
    cartButtonText: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        ProductInfo(
            modifier = Modifier.weight(4f),
            title = title,
            description = description,
            rate = rate,
            onFavoriteBtnClicked = onFavoriteBtnClicked,
            isProductFavorite = isProductFavorite
        )
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Black)
        AddToCartRow(
            modifier = Modifier.weight(1f),
            price = price,
            onAddToCartClicked = onAddToCartClicked,
            cartButtonText = cartButtonText
        )
    }
}