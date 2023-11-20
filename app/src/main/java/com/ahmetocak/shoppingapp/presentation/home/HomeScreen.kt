package com.ahmetocak.shoppingapp.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.presentation.home.components.CategoryList
import com.ahmetocak.shoppingapp.presentation.home.components.ErrorView
import com.ahmetocak.shoppingapp.presentation.home.components.PageHeader
import com.ahmetocak.shoppingapp.presentation.home.components.ProductList

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
            PageHeader(modifier = modifier, onShoppingCartClicked = onShoppingCartClicked)
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
                onProductClick = onProductClick,
                allCatText = ALL
            )
        }
    }
}