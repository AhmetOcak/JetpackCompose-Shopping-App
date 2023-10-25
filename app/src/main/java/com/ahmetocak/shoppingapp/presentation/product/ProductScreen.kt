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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R

@Composable
fun ProductScreen(modifier: Modifier = Modifier) {

    ProductScreenContent(modifier = modifier)
}

@Composable
private fun ProductScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ProductImage(
            modifier = modifier
                .weight(2f)
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.nine_level_margin))
        )
        ProductDetails(
            modifier = modifier
                .weight(1f)
                .fillMaxSize(),
            title = "Leset Galant",
            description = "Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight & soft fabric for breathable and comfortable wearing. And Solid stitched shirts with round neck made for durability and a great fit for casual fashion wear and diehard baseball fans. The Henley style round neckline includes a three-button placket.",
            price = 109.96
        )
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
                .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
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
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.one_level_margin)),
            text = "$$price",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.four_level_margin),
                    end = dimensionResource(id = R.dimen.one_level_margin)
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
private fun ProductImage(modifier: Modifier) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg")
            .crossfade(true).build(),
        contentDescription = null,
        error = painterResource(id = R.drawable.error_image),
        contentScale = ContentScale.Fit
    )
}