package com.ahmetocak.shoppingapp.presentation.home.search

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingProductItem
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.presentation.home.HomeSections
import com.ahmetocak.shoppingapp.presentation.home.ShoppingAppBottomBar
import com.ahmetocak.shoppingapp.utils.CustomPreview

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
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
                value = searchValue,
                onValueChange = onSearchValChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                placeholder = {
                    Text(text = stringResource(id = R.string.search))
                }
            )
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    vertical = dimensionResource(id = R.dimen.two_level_margin)
                ),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin))
            ) {
                items(searchResult, key = { it.id }) {
                    ShoppingProductItem(
                        id = it.id,
                        title = it.title,
                        price = it.price,
                        description = it.description,
                        category = it.category,
                        image = it.image,
                        rate = it.rating,
                        count = it.count,
                        onProductClick = onProductClick
                    )
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

@Composable
private fun SearchResultEmptyView(
    imageId: Int,
    messageId: Int,
    imageSize: Dp = 112.dp
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(imageSize),
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin))
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
            text = stringResource(id = messageId),
            textAlign = TextAlign.Center
        )
    }
}

@CustomPreview
@Composable
private fun SearchScreenNoSearchPreview() {
    ShoppingAppTheme {
        Surface {
            SearchScreenContent(
                modifier = Modifier,
                searchValue = "",
                onSearchValChanged = {},
                searchResult = listOf(),
                isSearchResultEmpty = false,
                onProductClick = {}
            )
        }
    }
}

@CustomPreview
@Composable
private fun SearchScreenPreview() {
    ShoppingAppTheme {
        Surface {
            SearchScreenContent(
                modifier = Modifier,
                searchValue = "",
                onSearchValChanged = {},
                searchResult = List(3) {
                    ProductEntity(
                        it,
                        "Preview Title",
                        (10 * it).toString(),
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                },
                isSearchResultEmpty = false,
                onProductClick = {}
            )
        }
    }
}

@CustomPreview
@Composable
private fun SearchScreenEmptyPreview() {
    ShoppingAppTheme {
        Surface {
            SearchScreenContent(
                modifier = Modifier,
                searchValue = "",
                onSearchValChanged = {},
                searchResult = listOf(),
                isSearchResultEmpty = true,
                onProductClick = {}
            )
        }
    }
}