package com.ahmetocak.shoppingapp.presentation.home.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.data.mapper.toProduct
import com.ahmetocak.shoppingapp.designsystem.components.FullScreenCircularLoading
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import com.ahmetocak.shoppingapp.presentation.home.favorites.components.EmptyFavoriteListView
import com.ahmetocak.shoppingapp.presentation.home.favorites.components.FavoriteItem
import com.ahmetocak.shoppingapp.presentation.home.HomeSections
import com.ahmetocak.shoppingapp.presentation.home.ShoppingAppBottomBar

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit,
    onNavigateRoute: (String) -> Unit,
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

    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.FAVORITES.route,
                navigateToRoute = onNavigateRoute
            )
        }
    ) { paddingValues ->
        FavoritesScreenContent(
            modifier = Modifier.padding(paddingValues),
            isLoading = uiState.isLoading,
            favoriteProductList = uiState.favoriteList,
            onRemoveFavoriteClicked = viewModel::removeProductFromFavorites,
            onFavoriteItemClicked = onProductClick
        )
    }
}

@Composable
private fun FavoritesScreenContent(
    modifier: Modifier,
    isLoading: Boolean,
    favoriteProductList: List<ProductEntity>,
    onRemoveFavoriteClicked: (Int?) -> Unit,
    onFavoriteItemClicked: (Product) -> Unit
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                FullScreenCircularLoading()
            }

            if (favoriteProductList.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.two_level_margin)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin))
                ) {
                    items(favoriteProductList, key = { it.id }) {
                        FavoriteItem(
                            imgUrl = it.image ?: "",
                            title = it.title ?: "",
                            price = it.price?.toDouble() ?: 0.0,
                            onRemoveFavoriteClicked = {
                                onRemoveFavoriteClicked(it.id)
                            },
                            onFavoriteItemClicked = {
                                onFavoriteItemClicked(it.toProduct())
                            }
                        )
                    }
                }
            } else {
                EmptyFavoriteListView(messageId = R.string.no_favorite)
            }
        }
    }
}