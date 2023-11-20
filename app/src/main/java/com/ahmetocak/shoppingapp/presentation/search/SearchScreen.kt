package com.ahmetocak.shoppingapp.presentation.search

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.mapper.toProduct
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingProductItem
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import com.ahmetocak.shoppingapp.presentation.search.components.SearchField
import com.ahmetocak.shoppingapp.presentation.search.components.SearchResultEmptyView

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onNavigateProductScreen: (Product) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessages.first().asString(),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.errorConsumed()
    }

    SearchScreenContent(
        modifier = modifier,
        searchValue = viewModel.searchedText,
        onSearchValChanged = {
            viewModel.updateSearchedText(it)
            viewModel.searchProductList()
        },
        searchResult = uiState.searchResult,
        isSearchResultEmpty = uiState.isSearchResultEmpty,
        onProductClick = onNavigateProductScreen
    )
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchField(modifier, searchValue, onSearchValChanged)
        LazyVerticalGrid(
            modifier = modifier.fillMaxWidth(),
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
                modifier,
                R.drawable.search_result_empty,
                R.string.search_result_empty_message
            )
        } else if (searchResult.isEmpty()) {
            SearchResultEmptyView(
                modifier,
                R.drawable.search,
                R.string.search_something
            )
        }
    }
}