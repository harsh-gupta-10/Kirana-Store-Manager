package com.kiranstore.manager.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageCompressionUtil @Inject constructor() {

    fun compressToWebP(context: Context, uri: Uri, maxWidth: Int = 512, quality: Int = 80): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream for URI")
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val resizedBitmap = resizeImage(originalBitmap, maxWidth)
        val outputStream = ByteArrayOutputStream()

        @Suppress("DEPRECATION")
        resizedBitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream)

        if (resizedBitmap != originalBitmap) {
            resizedBitmap.recycle()
        }
        originalBitmap.recycle()

        return outputStream.toByteArray()
    }

    fun resizeImage(bitmap: Bitmap, maxWidth: Int): Bitmap {
        if (bitmap.width <= maxWidth) return bitmap
        val ratio = maxWidth.toFloat() / bitmap.width.toFloat()
        val newHeight = (bitmap.height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, maxWidth, newHeight, true)
    }
}
