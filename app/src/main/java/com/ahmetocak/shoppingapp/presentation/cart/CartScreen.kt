package com.ahmetocak.shoppingapp.presentation.cart

import android.widget.Toast
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import com.ahmetocak.shoppingapp.ui.components.CartItemNumber

@Composable
fun CartScreen(modifier: Modifier = Modifier) {

    val viewModel: CartViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            stringResource(id = uiState.errorMessages.first()),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.consumedErrorMessage()
    }

    if (uiState.userMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            stringResource(id = uiState.userMessages.first()),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.consumedUserMessage()
    }

    CartScreenContent(
        modifier = modifier,
        cartList = uiState.cartList,
        onRemoveItemClick = {
            viewModel.removeProductFromCart(it)
        },
        subtotal = uiState.subtotal
    )
}

@Composable
private fun CartScreenContent(
    modifier: Modifier,
    cartList: List<CartEntity>,
    onRemoveItemClick: (Int) -> Unit,
    subtotal: Double
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
            .padding(bottom = dimensionResource(id = R.dimen.two_level_margin))
    ) {
        CartList(
            modifier = modifier.weight(4f),
            cartList = cartList,
            onRemoveItemClick = onRemoveItemClick
        )
        CheckoutDetails(modifier = modifier.weight(1f), subtotal = subtotal)
        CheckOutButton(modifier = modifier, subtotal = subtotal)
    }
}

@Composable
private fun CheckOutButton(modifier: Modifier, subtotal: Double) {
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
                    append(" $$subtotal")
                }
            },
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun CheckoutDetails(modifier: Modifier, subtotal: Double) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(id = R.string.sub_total))
            Text(text = "$$subtotal", fontWeight = FontWeight.Bold)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(id = R.string.delivery_fee))
            Text(text = "$5.00", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CartList(
    modifier: Modifier,
    cartList: List<CartEntity>,
    onRemoveItemClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.two_level_margin)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        items(cartList) { cart ->
            CartItem(
                id = cart.id,
                imageUrl = cart.image,
                title = cart.title,
                price = cart.price * cart.count,
                onRemoveItemClick = onRemoveItemClick
            )
        }
    }
}

@Composable
private fun CartItem(
    id: Int,
    imageUrl: String,
    title: String,
    price: Double,
    onRemoveItemClick: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            CartItemImg(imageUrl = imageUrl)
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                CartNameAndRemove(id = id, title = title, onRemoveItemClick = onRemoveItemClick)
                CartPriceAndCount(price)
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.two_level_margin),
                    bottom = dimensionResource(id = R.dimen.one_level_margin)
                )
        )
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
private fun CartNameAndRemove(id: Int, title: String, onRemoveItemClick: (Int) -> Unit) {
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
        IconButton(onClick = { onRemoveItemClick(id) }) {
            Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
        }
    }
}

@Composable
private fun CartItemImg(imageUrl: String) {
    Card(
        modifier = Modifier.size(128.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            1.dp, brush = Brush.horizontalGradient(
                listOf(
                    colorResource(id = R.color.dark_green),
                    colorResource(id = R.color.hunter_green),
                    colorResource(id = R.color.dark_moss_green),
                    colorResource(id = R.color.walnut_brown),
                    colorResource(id = R.color.bole),
                    colorResource(id = R.color.cordovan),
                    colorResource(id = R.color.redwood)
                )
            )
        )
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