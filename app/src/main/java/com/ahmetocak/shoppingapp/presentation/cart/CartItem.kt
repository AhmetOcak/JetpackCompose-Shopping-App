package com.ahmetocak.shoppingapp.presentation.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R

@Composable
fun CartItem(
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
                    onIncreaseClicked = remember { { onIncreaseClicked(id) } },
                    onDecreaseClicked = remember { { onDecreaseClicked(id) } }
                )
            }
        }
        Divider(
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
            fontWeight = FontWeight.Bold,
            color = Color.Black
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
            maxLines = 2,
            color = Color.Black
        )
        IconButton(onClick = { onRemoveItemClick(id) }) {
            Icon(imageVector = Icons.Filled.Clear, contentDescription = null, tint = Color.Black)
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
            contentScale = ContentScale.Fit,
            placeholder = if (LocalInspectionMode.current) painterResource(id = R.drawable.debug_placeholder) else null
        )
    }
}

@Composable
private fun CartItemCountSetter(
    itemCount: Int,
    onIncreaseClicked: () -> Unit,
    onDecreaseClicked: () -> Unit
) {
    var count by rememberSaveable { mutableIntStateOf(itemCount) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IcBtn(
            onClick = {
                if (count != 1) {
                    count--
                    onDecreaseClicked()
                }
            },
            tint = if (count != 1) {
                colorResource(id = R.color.green)
            } else {
                Color.Black
            },
            resourceId = R.drawable.ic_remove,
            enabled = count != 1,
            borderColor = if (count != 1) {
                colorResource(id = R.color.green)
            } else {
                Color.Gray
            }
        )
        Text(text = "$count", color = Color.Black)
        IcBtn(
            onClick = {
                if (count < 10) {
                    count++
                    onIncreaseClicked()
                }
            },
            tint = if (count < 10) {
                colorResource(id = R.color.green)
            } else {
                Color.Black
            },
            resourceId = R.drawable.ic_add,
            enabled = count < 10,
            borderColor = if (count < 10) {
                colorResource(id = R.color.green)
            } else {
                Color.Gray
            }
        )
    }
}

@Composable
private fun IcBtn(
    onClick: () -> Unit,
    tint: Color,
    borderColor: Color,
    resourceId: Int,
    enabled: Boolean
) {
    IconButton(
        modifier = Modifier.border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            tint = tint
        )
    }
}