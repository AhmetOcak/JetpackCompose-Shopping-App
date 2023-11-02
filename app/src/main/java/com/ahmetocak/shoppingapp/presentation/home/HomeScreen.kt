package com.ahmetocak.shoppingapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.model.shopping.Product
import com.ahmetocak.shoppingapp.ui.components.MinLineText

private const val ALL = "All"

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val viewModel: HomeViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        modifier = modifier,
        categories = uiState.categoryList,
        isCategoriesLoading = uiState.isCategoryListLoading,
        productList = uiState.productList,
        isProductListLoading = uiState.isProductListLoading
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    categories: List<String>,
    isCategoriesLoading: Boolean,
    productList: List<Product>,
    isProductListLoading: Boolean
) {
    var selectedCatName by rememberSaveable { mutableStateOf(ALL) }

    Column(modifier = modifier.fillMaxSize()) {
        PageTitle(modifier = modifier)
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
            selectedCatName = selectedCatName
        )
    }
}

@Composable
private fun ProductList(
    modifier: Modifier,
    productList: List<Product>,
    isProductListLoading: Boolean,
    selectedCatName: String
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
                    title = it.title ?: "null",
                    price = it.price ?: "null",
                    image = it.image ?: "null"
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
private fun PageTitle(modifier: Modifier) {
    Text(
        modifier = modifier.padding(
            start = dimensionResource(id = R.dimen.two_level_margin),
            top = dimensionResource(id = R.dimen.two_level_margin)
        ),
        text = stringResource(id = R.string.discover_products),
        style = MaterialTheme.typography.headlineMedium
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductItem(modifier: Modifier, title: String, price: String, image: String) {
    Card(
        modifier = modifier.fillMaxSize(),
        onClick = {},
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.one_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = modifier
                    .fillMaxWidth()
                    .height(128.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.error_image),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            MinLineText(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.one_level_margin))
                    .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = modifier.padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                text = "$$price"
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}