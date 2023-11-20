package com.ahmetocak.shoppingapp.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.mapper.toProduct
import com.ahmetocak.shoppingapp.designsystem.components.FullScreenCircularLoading
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import com.ahmetocak.shoppingapp.presentation.favorites.components.EmptyFavoriteListView
import com.ahmetocak.shoppingapp.presentation.favorites.components.FavoriteItem

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onNavigateProductScreen: (Product) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    viewModel.getAllFavoriteProducts()

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.errorMessageConsumed()
    }

    if (uiState.userMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.userMessages.first().asString())
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
            FullScreenCircularLoading()
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