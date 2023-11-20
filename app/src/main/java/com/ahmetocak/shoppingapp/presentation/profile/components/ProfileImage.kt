package com.ahmetocak.shoppingapp.presentation.profile.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ahmetocak.shoppingapp.R

@Composable
fun ProfileImage(onUserPhotoClicked: () -> Unit, userImgUrl: Uri?) {
    AsyncImage(
        modifier = Modifier
            .size(128.dp)
            .clip(CircleShape)
            .clickable(onClick = onUserPhotoClicked),
        model = ImageRequest.Builder(LocalContext.current)
            .data(userImgUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.empty_profile_img),
        placeholder = painterResource(id = R.drawable.empty_profile_img)
    )
}