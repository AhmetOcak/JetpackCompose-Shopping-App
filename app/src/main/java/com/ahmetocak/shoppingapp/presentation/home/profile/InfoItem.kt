package com.ahmetocak.shoppingapp.presentation.home.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetocak.shoppingapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoItem(
    modifier: Modifier = Modifier,
    imageId: Int,
    titleId: Int,
    infoType: InfoType,
    description: String,
    onAccountInfoClicked: (InfoType) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp),
        onClick = remember { { onAccountInfoClicked(infoType) } },
        shape = RoundedCornerShape(0),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.two_level_margin))
        ) {
            Image(
                modifier = modifier.size(48.dp),
                painter = painterResource(id = imageId),
                contentDescription = null,
                colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(colorResource(id = R.color.orange_500)) else null
            )
            Column(modifier = modifier.fillMaxWidth()) {
                Text(text = stringResource(id = titleId), fontWeight = FontWeight.Bold)
                Text(text = description, style = TextStyle(color = Color.Gray))
            }
        }
    }
}