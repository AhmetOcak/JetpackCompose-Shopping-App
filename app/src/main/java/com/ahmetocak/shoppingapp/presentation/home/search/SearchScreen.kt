package com.ahmetocak.shoppingapp.presentation.home.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingProductItem
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import com.ahmetocak.shoppingapp.presentation.home.HomeSections
import com.ahmetocak.shoppingapp.presentation.home.ShoppingAppBottomBar
import com.ahmetocak.shoppingapp.presentation.home.search.components.SearchField
import com.ahmetocak.shoppingapp.presentation.home.search.components.SearchResultEmptyView

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit,
    onNavigateRoute: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.errorConsumed()
    }

    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.SEARCH.route,
                navigateToRoute = onNavigateRoute
            )
        }
    ) { paddingValues ->
        SearchScreenContent(
            modifier = Modifier.padding(paddingValues),
            searchValue = viewModel.searchedText,
            onSearchValChanged = viewModel::onSearchValueChange,
            searchResult = uiState.searchResult,
            isSearchResultEmpty = uiState.isSearchResultEmpty,
            onProductClick = onProductClick
        )
    }
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier,
    searchValue: String,
    onSearchValChanged: (String) -> Unit,
    searchResult: List<ProductEntity>,
    isSearchResultEmpty: Boolean,
    onProductClick: (Product) -> Unit
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchField(searchValue = searchValue, onSearchValChanged = onSearchValChanged)
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    vertical = dimensionResource(id = R.dimen.two_level_margin)
                ),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin))
            ) {
                items(searchResult) {
                    ShoppingProductItem(product = it.toProduct(), onProductClick = onProductClick)
                }
            }

            if (isSearchResultEmpty) {
                SearchResultEmptyView(
                    R.drawable.search_result_empty,
                    R.string.search_result_empty_message
                )
            } else if (searchResult.isEmpty()) {
                SearchResultEmptyView(
                    R.drawable.search,
                    R.string.search_something
                )
            }
        }
    }
}