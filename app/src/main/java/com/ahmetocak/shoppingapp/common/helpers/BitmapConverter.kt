package com.ahmetocak.shoppingapp.common.helpers

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

fun imageBitmapToFile(context: Context, imageBitmap: ImageBitmap): Uri? {
    val bitmap: Bitmap = imageBitmap.asAndroidBitmap()
    val wrapper = ContextWrapper(context)
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)
    file = File(file, "image.jpg")

    try {
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }

    return Uri.fromFile(file)
}