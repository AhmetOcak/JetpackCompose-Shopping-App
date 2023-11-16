package com.ahmetocak.shoppingapp.presentation.favorites

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.mapper.toProduct
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier, onNavigateProductScreen: (Product) -> Unit) {

    val viewModel: FavoritesViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    viewModel.getAllFavoriteProducts()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessages.first().asString(),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.errorMessageConsumed()
    }

    if (uiState.userMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.userMessages.first().asString(),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.userMessagesConsumed()
    }

    FavoritesScreenContent(
        modifier = modifier,
        isLoading = uiState.isLoading,
        favoriteProductList = uiState.favoriteList,
        onRemoveFavoriteClicked = { id -> id?.let { viewModel.removeProductFromFavorites(it) } },
        onFavoriteItemClicked = { entity -> onNavigateProductScreen(entity.toProduct()) }
    )
}

@Composable
private fun FavoritesScreenContent(
    modifier: Modifier,
    isLoading: Boolean,
    favoriteProductList: List<ProductEntity>,
    onRemoveFavoriteClicked: (Int?) -> Unit,
    onFavoriteItemClicked: (ProductEntity) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (favoriteProductList.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.two_level_margin)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin))
            ) {
                items(favoriteProductList) {
                    FavoriteItem(
                        modifier = modifier,
                        imgUrl = it.image ?: "",
                        title = it.title ?: "",
                        price = it.price?.toDouble() ?: 0.0,
                        onRemoveFavoriteClicked = {
                            onRemoveFavoriteClicked(it.id)
                        },
                        onFavoriteItemClicked = {
                            onFavoriteItemClicked(it)
                        }
                    )
                }
            }
        } else {
            EmptyFavoriteListView(modifier = modifier, messageId = R.string.no_favorite)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteItem(
    modifier: Modifier,
    imgUrl: String,
    title: String,
    price: Double,
    onRemoveFavoriteClicked: () -> Unit,
    onFavoriteItemClicked: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(
            colorResource(id = R.color.dark_green),
            colorResource(id = R.color.hunter_green),
            colorResource(id = R.color.dark_moss_green),
            colorResource(id = R.color.walnut_brown),
            colorResource(id = R.color.bole),
            colorResource(id = R.color.cordovan),
            colorResource(id = R.color.redwood),
        ))),
        onClick = onFavoriteItemClicked
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.one_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(onClick = onRemoveFavoriteClicked) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            }
            AsyncImage(
                modifier = modifier
                    .height(128.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imgUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.error_image),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.one_level_margin))
                    .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                minLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )
            Text(
                modifier = modifier.padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                text = "$$price",
                color = Color.Black
            )
        }
    }
}

@Composable
private fun EmptyFavoriteListView(
    modifier: Modifier,
    messageId: Int,
    imageSize: Dp = 112.dp
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = modifier.size(imageSize),
            painter = painterResource(id = R.drawable.search_result_empty),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin))
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
            text = stringResource(id = messageId),
            textAlign = TextAlign.Center
        )
    }
}