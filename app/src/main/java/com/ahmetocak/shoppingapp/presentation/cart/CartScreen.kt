package com.ahmetocak.shoppingapp.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.model.shopping.CartEntity
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingScaffold
import com.ahmetocak.shoppingapp.presentation.designsystem.components.ShoppingShowToastMessage
import com.ahmetocak.shoppingapp.presentation.designsystem.theme.ShoppingAppTheme
import com.ahmetocak.shoppingapp.utils.CustomPreview
import com.ahmetocak.shoppingapp.utils.DELIVERY_FEE

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onPaymentClick: (Float) -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.consumedErrorMessage()
    }

    ShoppingScaffold(
        modifier = modifier
    ) { paddingValues ->
        CartScreenContent(
            modifier = Modifier.padding(paddingValues),
            cartList = uiState.cartList,
            onRemoveItemClick = remember(viewModel) { viewModel::removeProductFromCart },
            subtotal = uiState.subtotal,
            onIncreaseClicked = remember(viewModel) { viewModel::increaseProductCount },
            onDecreaseClicked = remember(viewModel) { viewModel::decreaseProductCount },
            onCheckoutBtnClicked = remember {
                { onPaymentClick((uiState.subtotal).toFloat()) }
            }
        )
    }
}

@Composable
private fun CartScreenContent(
    modifier: Modifier,
    cartList: List<CartEntity>,
    onRemoveItemClick: (Int) -> Unit,
    subtotal: Double,
    onIncreaseClicked: (Int) -> Unit,
    onDecreaseClicked: (Int) -> Unit,
    onCheckoutBtnClicked: () -> Unit
) {
    if (cartList.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = dimensionResource(id = R.dimen.two_level_margin))
        ) {
            CartList(
                modifier = Modifier.weight(4f),
                cartList = cartList,
                onRemoveItemClick = onRemoveItemClick,
                onDecreaseClicked = onDecreaseClicked,
                onIncreaseClicked = onIncreaseClicked
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(top = dimensionResource(id = R.dimen.two_level_margin))
                    .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.sub_total))
                    Text(text = "$${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.delivery_fee))
                    Text(
                        text = "$${String.format("%.2f", DELIVERY_FEE)}",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            CheckOutButton(subtotal = subtotal, onCheckoutBtnClicked = onCheckoutBtnClicked)
        }
    } else {
        EmptyCartListView(modifier = modifier, messageId = R.string.cart_empty)
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
    LazyColumn(
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
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.two_level_margin)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
    ) {
        items(cartList, key = { it.id }) { cart ->
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
}

@Composable
private fun EmptyCartListView(
    modifier: Modifier, messageId: Int, imageSize: Dp = 112.dp
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

@Composable
private fun CheckOutButton(
    subtotal: Double, onCheckoutBtnClicked: () -> Unit
) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
            .padding(bottom = dimensionResource(id = R.dimen.two_level_margin))
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
        onClick = onCheckoutBtnClicked,
        contentPadding = PaddingValues(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.checkout))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" $${String.format("%.2f", subtotal + DELIVERY_FEE)}")
                }
            }, style = MaterialTheme.typography.titleMedium
        )
    }
}

@CustomPreview
@Composable
private fun CartScreenPreview() {
    ShoppingAppTheme {
        Surface {
            CartScreenContent(
                modifier = Modifier,
                cartList = listOf(
                    CartEntity(0, "This is a preview title", price = 10.0, image = "", count = 1)
                ),
                onRemoveItemClick = {},
                subtotal = 10.0,
                onIncreaseClicked = {},
                onDecreaseClicked = {},
                onCheckoutBtnClicked = {}
            )
        }
    }
}

@CustomPreview
@Composable
private fun CartScreenEmptyCartPreview() {
    ShoppingAppTheme {
        Surface {
            CartScreenContent(
                modifier = Modifier,
                cartList = listOf(),
                onRemoveItemClick = {},
                subtotal = 0.0,
                onIncreaseClicked = {},
                onDecreaseClicked = {},
                onCheckoutBtnClicked = {}
            )
        }
    }
}