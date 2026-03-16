package com.kiranstore.manager.util

import android.graphics.Bitmap
import android.os.Build
import java.io.ByteArrayOutputStream

object ImageCompressor {

    /**
     * Compress a Bitmap to WebP format at the specified quality.
     *
     * @param bitmap The source bitmap to compress.
     * @param quality Compression quality (0-100). Default is 80.
     * @return Compressed image bytes in WebP format.
     */
    fun compressToWebP(bitmap: Bitmap, quality: Int = 80): ByteArray {
        val outputStream = ByteArrayOutputStream()

        @Suppress("DEPRECATION")
        val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }

        bitmap.compress(format, quality, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * Scale a bitmap to fit within max dimensions while maintaining aspect ratio,
     * then compress to WebP.
     *
     * @param bitmap The source bitmap.
     * @param maxWidth Maximum width in pixels. Default is 1920.
     * @param maxHeight Maximum height in pixels. Default is 1080.
     * @param quality Compression quality (0-100). Default is 80.
     * @return Compressed image bytes in WebP format.
     */
    fun scaleAndCompressToWebP(
        bitmap: Bitmap,
        maxWidth: Int = 1920,
        maxHeight: Int = 1080,
        quality: Int = 80
    ): ByteArray {
        val scaledBitmap = scaleBitmap(bitmap, maxWidth, maxHeight)
        return compressToWebP(scaledBitmap, quality)
    }

    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxWidth && height <= maxHeight) return bitmap

        val ratioW = maxWidth.toFloat() / width
        val ratioH = maxHeight.toFloat() / height
        val scale = minOf(ratioW, ratioH)

        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
