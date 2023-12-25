package com.ahmetocak.shoppingapp.presentation.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.model.shopping.CartEntity

@Composable
fun CartList(
    modifier: Modifier,
    cartList: List<CartEntity>,
    onRemoveItemClick: (Int) -> Unit,
    onIncreaseClicked: (Int) -> Unit,
    onDecreaseClicked: (Int) -> Unit
) {
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