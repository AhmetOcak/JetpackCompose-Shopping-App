package com.ahmetocak.shoppingapp.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {

    ProfileScreenContent(modifier = modifier)
}

@Composable
private fun ProfileScreenContent(modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        ProfileSection(modifier = modifier.weight(3f))
        AccountInfoSection(modifier = modifier.weight(4f))
    }
}

@Composable
private fun ProfileSection(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorResource(id = R.color.orange_100),
                        colorResource(id = R.color.orange_200),
                        colorResource(id = R.color.orange_300),
                        colorResource(id = R.color.orange_400),
                        colorResource(id = R.color.orange_500)
                    ),
                    tileMode = TileMode.Decal
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://images.pexels.com/photos/8159657/pexels-photo-8159657.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.six_level_margin))
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            text = "Ahmet Ocak",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AccountInfoSection(modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
                .padding(top = dimensionResource(id = R.dimen.four_level_margin)),
            text = "Account Info",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.three_level_margin))
        ) {
            items(accountInfoList) {
                Info(
                    imageId = it.imageId,
                    titleId = it.titleId,
                    description = "ocak6139@gmail.com"
                )
                Divider(modifier = modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Info(modifier: Modifier = Modifier, imageId: Int, titleId: Int, description: String) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp),
        onClick = {},
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
                contentDescription = null
            )
            Column(modifier = modifier.fillMaxWidth()) {
                Text(text = stringResource(id = titleId), fontWeight = FontWeight.Bold)
                Text(text = description, style = TextStyle(color = Color.Gray))
            }
        }
    }
}

private val accountInfoList = listOf(
    AccountInfo(R.string.name, R.drawable.ic_name),
    AccountInfo(R.string.mobile, R.drawable.ic_mobile),
    AccountInfo(R.string.email, R.drawable.ic_email),
    AccountInfo(R.string.address, R.drawable.ic_address),
    AccountInfo(R.string.dob, R.drawable.ic_dob)
)

data class AccountInfo(
    val titleId: Int,
    val imageId: Int
)