package com.ahmetocak.shoppingapp.presentation.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.ui.components.CartItemNumber

@Composable
fun CartScreen(modifier: Modifier = Modifier) {

    CartScreenContent(modifier = modifier)
}

@Composable
private fun CartScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
            .padding(bottom = dimensionResource(id = R.dimen.two_level_margin))
    ) {
        CartList(modifier = modifier.weight(4f))
        CheckoutDetails(modifier = modifier.weight(1f))
        CheckOutButton(modifier = modifier)
    }
}

@Composable
private fun CheckOutButton(modifier: Modifier) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.two_level_margin))
    )
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = { /*TODO*/ },
        contentPadding = PaddingValues(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.checkout))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" $805.00")
                }
            }, style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun CheckoutDetails(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(id = R.string.sub_total))
            Text(text = "$800.00", fontWeight = FontWeight.Bold)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(id = R.string.delivery_fee))
            Text(text = "$5.00", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CartList(modifier: Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.two_level_margin)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        items(8) {
            CartItem(
                imageUrl = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
                title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                price = 109.43
            )
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.two_level_margin),
                        bottom = dimensionResource(id = R.dimen.one_level_margin)
                    )
            )
        }
    }
}

@Composable
private fun CartItem(imageUrl: String, title: String, price: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp),
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.one_level_margin)
        )
    ) {
        CartItemImg(imageUrl = imageUrl)
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            CartNameAndRemove(title = title)
            CartPriceAndCount(price)
        }
    }
}

@Composable
private fun CartPriceAndCount(price: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "$$price",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        CartItemNumber()
    }
}

@Composable
private fun CartNameAndRemove(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.Clear, contentDescription = null
            )
        }
    }
}

@Composable
private fun CartItemImg(imageUrl: String) {
    Card(
        modifier = Modifier.size(128.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, brush = Brush.horizontalGradient(listOf(
            colorResource(id = R.color.dark_green),
            colorResource(id = R.color.hunter_green),
            colorResource(id = R.color.dark_moss_green),
            colorResource(id = R.color.walnut_brown),
            colorResource(id = R.color.bole),
            colorResource(id = R.color.cordovan),
            colorResource(id = R.color.redwood)
        )))
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.two_level_margin)),
            model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}