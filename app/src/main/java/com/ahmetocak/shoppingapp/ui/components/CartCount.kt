package com.ahmetocak.shoppingapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R

@Composable
fun CartItemNumber() {
    var count by rememberSaveable { mutableStateOf(1) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IcBtn(
            onClick = {
                if (count != 1) {
                    count--
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
        Text(text = "$count")
        IcBtn(
            onClick = {
                if (count < 10) {
                    count++
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