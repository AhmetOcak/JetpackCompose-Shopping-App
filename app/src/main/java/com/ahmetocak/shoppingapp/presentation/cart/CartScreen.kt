package com.ahmetocak.shoppingapp.presentation.cart

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import com.ahmetocak.shoppingapp.ui.components.CartItemCountSetter

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
        subtotal = uiState.subtotal,
        onIncreaseClicked = {
            viewModel.increaseProductCount(it)
        },
        onDecreaseClicked = {
            viewModel.decreaseProductCount(it)
        }
    )
}

@Composable
private fun CartScreenContent(
    modifier: Modifier,
    cartList: List<CartEntity>,
    onRemoveItemClick: (Int) -> Unit,
    subtotal: Double,
    onIncreaseClicked: (Int) -> Unit,
    onDecreaseClicked: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = dimensionResource(id = R.dimen.two_level_margin))
    ) {
        CartList(
            modifier = modifier.weight(4f),
            cartList = cartList,
            onRemoveItemClick = onRemoveItemClick,
            onDecreaseClicked = onDecreaseClicked,
            onIncreaseClicked = onIncreaseClicked
        )
        CheckoutDetails(
            modifier = modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            subtotal = subtotal
        )
        CheckOutButton(
            modifier = modifier
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            subtotal = subtotal
        )
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
                    append(" $${String.format("%.2f", subtotal)}")
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
            Text(text = "$${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Bold)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(id = R.string.delivery_fee))
            Text(text = "$${String.format("%.2f", 5.00)}", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CartList(
    modifier: Modifier,
    cartList: List<CartEntity>,
    onRemoveItemClick: (Int) -> Unit,
    onIncreaseClicked: (Int) -> Unit,
    onDecreaseClicked: (Int) -> Unit
) {
    if (cartList.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            colorResource(id = R.color.mauve),
                            colorResource(id = R.color.pale_purple),
                        ),
                    )
                ),
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.two_level_margin)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            items(cartList) { cart ->
                CartItem(
                    id = cart.id,
                    imageUrl = cart.image,
                    title = cart.title,
                    price = cart.price * cart.count,
                    onRemoveItemClick = onRemoveItemClick,
                    itemCount = cart.count,
                    onIncreaseClicked = onIncreaseClicked,
                    onDecreaseClicked = onDecreaseClicked
                )
            }
        }
    } else {
        EmptyCartListView(modifier = modifier, messageId = R.string.cart_empty)
    }
}

@Composable
private fun CartItem(
    id: Int,
    imageUrl: String,
    title: String,
    price: Double,
    onRemoveItemClick: (Int) -> Unit,
    itemCount: Int,
    onIncreaseClicked: (Int) -> Unit,
    onDecreaseClicked: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            CartItemImg(imageUrl = imageUrl)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                CartNameAndRemove(id = id, title = title, onRemoveItemClick = onRemoveItemClick)
                CartPriceAndCount(
                    itemCount = itemCount,
                    price = price,
                    onIncreaseClicked = { onIncreaseClicked(id) },
                    onDecreaseClicked = { onDecreaseClicked(id) }
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.two_level_margin),
                    bottom = dimensionResource(id = R.dimen.one_level_margin)
                ),
            color = Color.Magenta
        )
    }
}

@Composable
private fun CartPriceAndCount(
    price: Double,
    itemCount: Int,
    onIncreaseClicked: () -> Unit,
    onDecreaseClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "$${String.format("%.2f", price)}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        CartItemCountSetter(
            itemCount = itemCount,
            onIncreaseClicked = onIncreaseClicked,
            onDecreaseClicked = onDecreaseClicked
        )
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

@Composable
private fun EmptyCartListView(
    modifier: Modifier,
    messageId: Int,
    imageSize: Dp = 112.dp
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(imageSize),
            painter = painterResource(id = R.drawable.search_result_empty),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin))
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
            text = stringResource(id = messageId),
            textAlign = TextAlign.Center
        )
    }
}