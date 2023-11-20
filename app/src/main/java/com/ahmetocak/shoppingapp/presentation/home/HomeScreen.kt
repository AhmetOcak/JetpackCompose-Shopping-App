package com.ahmetocak.shoppingapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.designsystem.components.ProductItem

private const val ALL = "All"

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateProductScreen: (Product) -> Unit,
    onNavigateCartScreen: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        modifier = modifier,
        categories = uiState.categoryList,
        isCategoriesLoading = uiState.isCategoryListLoading,
        productList = uiState.productList,
        isProductListLoading = uiState.isProductListLoading,
        onProductClick = { onNavigateProductScreen(it) },
        onShoppingCartClicked = { onNavigateCartScreen() },
        errors = uiState.errorMessages
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    categories: List<String>,
    isCategoriesLoading: Boolean,
    productList: List<Product>,
    isProductListLoading: Boolean,
    onProductClick: (Product) -> Unit,
    onShoppingCartClicked: () -> Unit,
    errors: List<UiText>
) {
    var selectedCatName by rememberSaveable { mutableStateOf(ALL) }

    Column(modifier = modifier.fillMaxSize()) {
        if (errors.isNotEmpty()) {
            ErrorView(modifier, errors)
        } else {
            PageTitleAndCartBtn(modifier = modifier, onShoppingCartClicked = onShoppingCartClicked)
            CategoryList(
                modifier = modifier,
                categories = categories,
                isCategoriesLoading = isCategoriesLoading,
                selectedCatName = selectedCatName,
                onCategoryClick = { selectedCatName = it }
            )
            ProductList(
                modifier = modifier,
                productList = productList,
                isProductListLoading = isProductListLoading,
                selectedCatName = selectedCatName,
                onProductClick = onProductClick
            )
        }
    }
}

@Composable
private fun ErrorView(
    modifier: Modifier,
    errors: List<UiText>
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.error_img),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                text = errors.first().asString(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProductList(
    modifier: Modifier,
    productList: List<Product>,
    isProductListLoading: Boolean,
    selectedCatName: String,
    onProductClick: (Product) -> Unit
) {
    if (isProductListLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.one_level_margin)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            items(
                if (selectedCatName == ALL) productList
                else productList.filter { it.category?.uppercase() == selectedCatName.uppercase() }
            ) {
                ProductItem(
                    modifier = modifier,
                    product = it,
                    onProductClick = onProductClick
                )
            }
        }
    }
}

@Composable
private fun CategoryList(
    modifier: Modifier,
    categories: List<String>,
    isCategoriesLoading: Boolean,
    selectedCatName: String,
    onCategoryClick: (String) -> Unit
) {
    if (isCategoriesLoading) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            item {
                Category(
                    categoryName = stringResource(id = R.string.all),
                    selectedCatName = selectedCatName,
                    onCategoryClick = onCategoryClick
                )
            }
            items(categories) { category ->
                Category(
                    categoryName = category.replaceFirstChar { it.uppercase() },
                    selectedCatName = selectedCatName,
                    onCategoryClick = onCategoryClick
                )
            }
        }
    }
}

@Composable
private fun Category(
    categoryName: String,
    selectedCatName: String,
    onCategoryClick: (String) -> Unit
) {
    Button(
        onClick = {
            onCategoryClick(categoryName)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (categoryName == selectedCatName) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            }
        )
    ) {
        Text(text = categoryName)
    }
}

@Composable
private fun PageTitleAndCartBtn(modifier: Modifier, onShoppingCartClicked: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier.padding(
                start = dimensionResource(id = R.dimen.two_level_margin),
                top = dimensionResource(id = R.dimen.two_level_margin)
            ),
            text = stringResource(id = R.string.discover_products),
            style = MaterialTheme.typography.headlineMedium
        )
        IconButton(onClick = onShoppingCartClicked) {
            Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = null)
        }
    }
}