package com.ahmetocak.shoppingapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    HomeScreenContent(modifier = modifier)
}

@Composable
private fun HomeScreenContent(modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        PageTitle(modifier = modifier)
        CategoryList(modifier = modifier)
        ProductList(modifier = modifier)
    }
}

@Composable
fun ProductList(modifier: Modifier) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.one_level_margin))
            .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.one_level_margin)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        items(10) {
            ProductItem(
                modifier = modifier,
                title = "Mens Casual Premium Slim Fit T-Shirts",
                price = 109.95
            )
        }
    }
}

@Composable
fun CategoryList(modifier: Modifier) {
    var selectedCatName by rememberSaveable { mutableStateOf("All") }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        item {
            Category(
                categoryName = stringResource(id = R.string.all),
                selectedCatName = selectedCatName,
                onCategoryClick = { selectedCatName = it })
        }
        items(10) {
            Category(
                categoryName = "temp",
                selectedCatName = selectedCatName,
                onCategoryClick = { selectedCatName = it })
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
            start = dimensionResource(id = R.dimen.one_level_margin),
            top = dimensionResource(id = R.dimen.one_level_margin)
        ),
        text = stringResource(id = R.string.discover_products),
        style = MaterialTheme.typography.headlineMedium
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(modifier: Modifier, title: String, price: Double) {
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
                    .data("https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg")
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.error_image),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.one_level_margin))
                    .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
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