package com.ahmetocak.shoppingapp.presentation.home.product

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
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingProductItem
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.presentation.home.HomeSections
import com.ahmetocak.shoppingapp.presentation.home.ShoppingAppBottomBar
import com.ahmetocak.shoppingapp.utils.CustomPreview

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
            categories = uiState.categoryUiState.categoryList,
            isCategoriesLoading = uiState.categoryUiState.isLoading,
            productList = uiState.productUiState.productList,
            isProductListLoading = uiState.productUiState.isLoading,
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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id = R.drawable.error_img),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                            text = errors.first().asString(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.two_level_margin),
                            top = dimensionResource(id = R.dimen.one_level_margin)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.discover_products),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    IconButton(onClick = onShoppingCartClicked) {
                        Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = null)
                    }
                }
                CategoryList(
                    categories = categories,
                    isCategoriesLoading = isCategoriesLoading,
                    selectedCatName = selectedCatName,
                    onCategoryClick = remember { { selectedCatName = it } }
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

@Composable
private fun CategoryList(
    categories: List<String>,
    isCategoriesLoading: Boolean,
    selectedCatName: String,
    onCategoryClick: (String) -> Unit
) {
    if (isCategoriesLoading) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            item {
                CategoryItem(
                    categoryName = stringResource(id = R.string.all),
                    selectedCatName = selectedCatName,
                    onCategoryClick = onCategoryClick
                )
            }
            items(categories, key = { it }) { category ->
                CategoryItem(
                    categoryName = category,
                    selectedCatName = selectedCatName,
                    onCategoryClick = onCategoryClick
                )
            }
        }
    }
}

@Composable
private fun ProductList(
    productList: List<Product>,
    isProductListLoading: Boolean,
    selectedCatName: String,
    onProductClick: (Product) -> Unit,
    allCatText: String
) {
    if (isProductListLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.one_level_margin)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            items(
                if (selectedCatName == allCatText) productList
                else productList.filter { it.category?.uppercase() == selectedCatName.uppercase() },
                key = { it.id }
            ) {
                ShoppingProductItem(
                    id = it.id,
                    title = it.title,
                    price = it.price,
                    description = it.description,
                    category = it.category,
                    image = it.image,
                    rate = it.rating?.rate,
                    count = it.rating?.count,
                    onProductClick = onProductClick
                )
            }
        }
    }
}

@CustomPreview
@Composable
private fun ProductScreenPreview() {
    ShoppingAppTheme {
        Surface {
            ProductScreenContent(
                modifier = Modifier,
                categories = listOf(
                    "technology",
                    "electronics",
                    "jewelery",
                    "men's clothing",
                    "women's clothing"
                ),
                isCategoriesLoading = false,
                productList = List(3) {
                    Product(
                        id = it,
                        title = "Product Title",
                        price = (10 * it).toString(),
                        description = null,
                        category = null,
                        image = null,
                        rating = null
                    )
                },
                isProductListLoading = false,
                onProductClick = {},
                onShoppingCartClicked = {},
                errors = listOf()
            )
        }
    }
}

@CustomPreview
@Composable
private fun ProductScreenErrorPreview() {
    ShoppingAppTheme {
        Surface {
            ProductScreenContent(
                modifier = Modifier,
                categories = listOf(),
                isCategoriesLoading = false,
                productList = listOf(),
                isProductListLoading = false,
                onProductClick = {},
                onShoppingCartClicked = {},
                errors = listOf(UiText.DynamicString("This is a fake error"))
            )
        }
    }
}