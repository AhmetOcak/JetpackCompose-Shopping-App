package com.ahmetocak.shoppingapp.presentation.home.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.helpers.UiText
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.presentation.home.HomeSections
import com.ahmetocak.shoppingapp.presentation.home.ShoppingAppBottomBar
import com.ahmetocak.shoppingapp.presentation.home.product.components.CategoryList
import com.ahmetocak.shoppingapp.presentation.home.product.components.ErrorView
import com.ahmetocak.shoppingapp.presentation.home.product.components.PageHeader
import com.ahmetocak.shoppingapp.presentation.home.product.components.ProductList

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit,
    onNavigateRoute: (String) -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.PRODUCT.route,
                navigateToRoute = onNavigateRoute
            )
        }
    ) { paddingValues ->
        ProductScreenContent(
            modifier = Modifier.padding(paddingValues),
            categories = uiState.categoryList,
            isCategoriesLoading = uiState.isCategoryListLoading,
            productList = uiState.productList,
            isProductListLoading = uiState.isProductListLoading,
            onProductClick = onProductClick,
            onShoppingCartClicked = onCartClick,
            errors = uiState.errorMessages
        )
    }
}

@Composable
private fun ProductScreenContent(
    modifier: Modifier,
    categories: List<String>,
    isCategoriesLoading: Boolean,
    productList: List<Product>,
    isProductListLoading: Boolean,
    onProductClick: (Product) -> Unit,
    onShoppingCartClicked: () -> Unit,
    errors: List<UiText>
) {
    val allKeyword = stringResource(id = R.string.all)
    var selectedCatName by rememberSaveable { mutableStateOf(allKeyword) }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (errors.isNotEmpty()) {
                ErrorView(modifier, errors)
            } else {
                PageHeader(onShoppingCartClicked = onShoppingCartClicked)
                CategoryList(
                    categories = categories,
                    isCategoriesLoading = isCategoriesLoading,
                    selectedCatName = selectedCatName,
                    onCategoryClick = { selectedCatName = it }
                )
                ProductList(
                    productList = productList,
                    isProductListLoading = isProductListLoading,
                    selectedCatName = selectedCatName,
                    onProductClick = onProductClick,
                    allCatText = stringResource(id = R.string.all)
                )
            }
        }
    }
}