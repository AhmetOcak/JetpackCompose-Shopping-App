package com.ahmetocak.shoppingapp.presentation.home.profile.dialogs

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.common.helpers.imageBitmapToFile
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog

@Composable
fun ImageCropper(imageUri: String?, context: Context, uploadPhoto: (Uri?) -> Unit) {
    val imageCropper = rememberImageCropper()
    val errorMessage = stringResource(id = R.string.unknown_error)

    if (imageUri != null) {
        LaunchedEffect(true) {
            when (val result = imageCropper.crop(Uri.parse(imageUri), context)) {
                CropResult.Cancelled -> {}
                is CropError -> {
                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is CropResult.Success -> {
                    uploadPhoto(imageBitmapToFile(context, result.bitmap))
                }
            }
        }
    }

    val cropState = imageCropper.cropState
    if (cropState != null) ImageCropperDialog(state = cropState)
}