package com.ahmetocak.shoppingapp.presentation.home.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.data.mapper.toProduct
import com.ahmetocak.shoppingapp.presentation.designsystem.components.FullScreenCircularLoading
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.presentation.home.HomeSections
import com.ahmetocak.shoppingapp.presentation.home.ShoppingAppBottomBar
import com.ahmetocak.shoppingapp.utils.CustomPreview

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit,
    onNavigateRoute: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.getAllFavoriteProducts()

    if (uiState.userMessages.isNotEmpty()) {
        val context = LocalContext.current
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(message = uiState.userMessages.first().asString(context))
            viewModel.userMessagesConsumed()
        }
    }

    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.FAVORITES.route,
                navigateToRoute = onNavigateRoute
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                            onRemoveFavoriteClicked = remember {
                                { onRemoveFavoriteClicked(it.id) }
                            },
                            onFavoriteItemClicked = remember {
                                { onFavoriteItemClicked(it.toProduct()) }
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.size(112.dp),
                        painter = painterResource(id = R.drawable.search_result_empty),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(id = R.dimen.two_level_margin))
                            .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
                        text = stringResource(id = R.string.no_favorite),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@CustomPreview
@Composable
private fun FavoritesScreenPreview() {
    ShoppingAppTheme {
        Surface {
            FavoritesScreenContent(
                modifier = Modifier,
                isLoading = false,
                favoriteProductList = listOf(
                    ProductEntity(
                        0,
                        "This is a preview title",
                        "10",
                        description = "This is a preview description",
                        category = "preview category",
                        image = null,
                        rating = 5.0,
                        count = null
                    )
                ),
                onRemoveFavoriteClicked = {},
                onFavoriteItemClicked = {}
            )
        }
    }
}

@CustomPreview
@Composable
private fun FavoritesScreenEmptyFavoriteListPreview() {
    ShoppingAppTheme {
        Surface {
            FavoritesScreenContent(
                modifier = Modifier,
                isLoading = false,
                favoriteProductList = listOf(),
                onRemoveFavoriteClicked = {},
                onFavoriteItemClicked = {}
            )
        }
    }
}