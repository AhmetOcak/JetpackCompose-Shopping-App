package com.ahmetocak.shoppingapp.presentation.search

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.mapper.toProduct
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import com.ahmetocak.shoppingapp.ui.components.ProductItem

@Composable
fun SearchScreen(modifier: Modifier = Modifier, onNavigateProductScreen: (Product) -> Unit) {

    val viewModel: SearchViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            stringResource(id = uiState.errorMessages.first()),
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
                ProductItem(product = it.toProduct(), onProductClick = onProductClick)
            }
        }

        if (isSearchResultEmpty) {
            SearchEmptyView(
                modifier,
                R.drawable.search_result_empty,
                R.string.search_result_empty_message
            )
        } else if (searchResult.isEmpty()) {
            SearchEmptyView(
                modifier,
                R.drawable.search,
                R.string.search_something
            )
        }
    }
}

@Composable
private fun SearchEmptyView(
    modifier: Modifier,
    imageId: Int,
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
            painter = painterResource(id = imageId),
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

@Composable
private fun SearchField(
    modifier: Modifier,
    searchValue: String,
    onSearchValChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        value = searchValue,
        onValueChange = onSearchValChanged,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        placeholder = {
            Text(text = stringResource(id = R.string.search))
        }
    )
}