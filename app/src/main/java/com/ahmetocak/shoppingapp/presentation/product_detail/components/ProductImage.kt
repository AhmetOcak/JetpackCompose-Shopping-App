package com.ahmetocak.shoppingapp.presentation.product_detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R

@Composable
fun ProductImage(modifier: Modifier, imgUrl: String) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current).data(imgUrl).crossfade(true).build(),
        contentDescription = null,
        error = painterResource(id = R.drawable.error_image),
        contentScale = ContentScale.Fit
    )
}