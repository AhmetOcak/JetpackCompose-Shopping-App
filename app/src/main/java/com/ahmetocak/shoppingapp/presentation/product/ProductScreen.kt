package com.ahmetocak.shoppingapp.presentation.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.model.shopping.Product

@Composable
fun ProductScreen(modifier: Modifier = Modifier) {

    val viewModel: ProductViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    ProductScreenContent(modifier = modifier, product = uiState.product)
}

@Composable
private fun ProductScreenContent(modifier: Modifier, product: Product?) {
    if (product != null) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            ProductImage(
                modifier = modifier
                    .weight(2f)
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.nine_level_margin)),
                imgUrl = product.image ?: "null"
            )
            ProductDetails(
                modifier = modifier
                    .weight(1f)
                    .fillMaxSize(),
                title = product.title ?: "null",
                description = product.description ?: "null",
                price = product.price?.toDouble() ?: 0.0
            )
        }
    }
}

@Composable
private fun ProductDetails(modifier: Modifier, title: String, description: String, price: Double) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            text = description
        )
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Black)
        AddToCartSection(modifier = Modifier.weight(2f), price = price)
    }
}

@Composable
private fun AddToCartSection(modifier: Modifier, price: Double) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.two_level_margin)),
            text = "$$price",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.four_level_margin),
                    end = dimensionResource(id = R.dimen.two_level_margin)
                ),
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(vertical = 12.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.add_to_cart),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun ProductImage(modifier: Modifier, imgUrl: String) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imgUrl)
            .crossfade(true).build(),
        contentDescription = null,
        error = painterResource(id = R.drawable.error_image),
        contentScale = ContentScale.Fit
    )
}