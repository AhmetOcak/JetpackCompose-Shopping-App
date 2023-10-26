package com.ahmetocak.shoppingapp.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {

    FavoritesScreenContent(modifier = modifier)
}

@Composable
private fun FavoritesScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.one_level_margin)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin))
        ) {
            items(10) {
                FavoriteItem(
                    modifier = modifier,
                    imgUrl = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
                    title = "My favorite item",
                    price = 100.00
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteItem(modifier: Modifier, imgUrl: String, title: String, price: Double) {
    Card(modifier = modifier) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.one_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                    }
                }
            }
            AsyncImage(
                modifier = modifier
                    .height(128.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imgUrl)
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