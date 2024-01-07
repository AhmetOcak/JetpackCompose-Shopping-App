package com.ahmetocak.shoppingapp.presentation.product_detail

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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
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
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.utils.CustomPreview

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

    ShoppingScaffold(modifier = modifier) { paddingValues ->
        ProductDetailScreenContent(
            modifier = Modifier.padding(paddingValues),
            title = uiState.product?.title ?: "",
            description = uiState.product?.description ?: "",
            price = uiState.product?.price?.toDouble() ?: 0.0,
            rate = uiState.product?.rating?.rate ?: 0.0,
            image = uiState.product?.image ?: "",
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
}

@Composable
private fun ProductDetailScreenContent(
    modifier: Modifier,
    title: String,
    description: String,
    price: Double,
    rate: Double,
    image: String,
    isProductFavorite: Boolean,
    onFavoriteBtnClicked: () -> Unit,
    onAddToCartClicked: () -> Unit,
    cartButtonText: String
) {
    Column(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.nine_level_margin)),
            model = ImageRequest.Builder(LocalContext.current).data(image)
                .crossfade(true).build(),
            contentDescription = null,
            error = painterResource(id = R.drawable.error_image),
            contentScale = ContentScale.Fit,
            placeholder = if (LocalInspectionMode.current) painterResource(id = R.drawable.debug_placeholder) else null
        )
        ProductDetails(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            title = title,
            description = description,
            price = price,
            rate = rate,
            isProductFavorite = isProductFavorite,
            onFavoriteBtnClicked = onFavoriteBtnClicked,
            onAddToCartClicked = onAddToCartClicked,
            cartButtonText = cartButtonText
        )
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
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
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
                .padding(top = dimensionResource(id = R.dimen.one_level_margin)),
            text = description
        )
    }
}

@CustomPreview
@Composable
private fun ProductDetailScreenPreview() {
    ShoppingAppTheme {
        Surface {
            ProductDetailScreenContent(
                modifier = Modifier,
                title = "Product Title",
                description = previewDescription,
                price = 120.0,
                rate = 4.3,
                image = "",
                isProductFavorite = false,
                onFavoriteBtnClicked = {},
                onAddToCartClicked = {},
                cartButtonText = "Add to Cart"
            )
        }
    }
}

@CustomPreview
@Composable
private fun ProductDetailScreenProductFavoritePreview() {
    ShoppingAppTheme {
        Surface {
            ProductDetailScreenContent(
                modifier = Modifier,
                title = "Product Title",
                description = previewDescription,
                price = 120.0,
                rate = 4.3,
                image = "",
                isProductFavorite = true,
                onFavoriteBtnClicked = {},
                onAddToCartClicked = {},
                cartButtonText = "Go to Cart"
            )
        }
    }
}

private const val previewDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
        "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."