package com.ahmetocak.shoppingapp.presentation.product

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.model.shopping.Product

@Composable
fun ProductScreen(modifier: Modifier = Modifier, onNavigateCartScreen: () -> Unit) {

    val viewModel: ProductViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.userMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.userMessages.first().asString(),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.consumedUserMessages()
    }

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessages.first().asString(),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.consumedErrorMessages()
    }

    ProductScreenContent(
        modifier = modifier,
        product = uiState.product,
        isProductFavorite = uiState.isProductFavorite,
        onFavoriteBtnClicked = viewModel::onFavoriteProductClick,
        onAddToCartClicked = {
            if (uiState.isProductInCart) {
                onNavigateCartScreen()
            } else {
                viewModel.addProductToCart()
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
private fun ProductScreenContent(
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
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Black)
        AddToCartSection(
            modifier = Modifier.weight(1f),
            price = price,
            onAddToCartClicked = onAddToCartClicked,
            cartButtonText = cartButtonText
        )
    }
}

@Composable
private fun ProductInfo(
    modifier: Modifier,
    title: String,
    description: String,
    rate: Double,
    isProductFavorite: Boolean,
    onFavoriteBtnClicked: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = colorResource(id = R.color.orange)
                )
                Text(text = "$rate")
            }
            IconButton(onClick = onFavoriteBtnClicked) {
                Icon(
                    imageVector = if (isProductFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
                .padding(top = dimensionResource(id = R.dimen.one_level_margin)),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
                .padding(top = dimensionResource(id = R.dimen.one_level_margin)), text = description
        )
    }
}

@Composable
private fun AddToCartSection(
    modifier: Modifier,
    price: Double,
    onAddToCartClicked: () -> Unit,
    cartButtonText: String
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.two_level_margin)),
            text = "$$price",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.four_level_margin),
                    end = dimensionResource(id = R.dimen.two_level_margin)
                ),
            onClick = onAddToCartClicked,
            contentPadding = PaddingValues(vertical = 12.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = cartButtonText,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun ProductImage(modifier: Modifier, imgUrl: String) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current).data(imgUrl).crossfade(true).build(),
        contentDescription = null,
        error = painterResource(id = R.drawable.error_image),
        contentScale = ContentScale.Fit
    )
}