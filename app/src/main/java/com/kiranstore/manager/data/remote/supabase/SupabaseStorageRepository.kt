package com.kiranstore.manager.data.remote.supabase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

@Singleton
class SupabaseStorageRepository @Inject constructor(
    private val client: SupabaseClient?
) {
    companion object {
        private const val BUCKET = "customer-images"
        private const val MAX_DIMENSION = 1280
        private const val QUALITY = 80
    }

    suspend fun uploadCustomerImage(
        customerId: Long,
        uri: Uri,
        context: Context
    ): Result<String> = withContext(Dispatchers.IO) {
        val supabase = client ?: return@withContext Result.failure(
            IllegalStateException("Supabase credentials are missing; cannot upload.")
        )

        val resolver = context.contentResolver
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, uri))
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(resolver, uri)
        }

        val scaled = scaleBitmap(bitmap)
        val webpBytes = compressToWebP(scaled)
        val path = "customers/$customerId/${System.currentTimeMillis()}.webp"

        return@withContext runCatching {
            supabase.storage.from(BUCKET).upload(path, webpBytes, upsert = true)
            supabase.storage.from(BUCKET).publicUrl(path)
        }
    }

    private fun scaleBitmap(source: Bitmap): Bitmap {
        val maxSide = max(source.width, source.height)
        if (maxSide <= MAX_DIMENSION) return source
        val scale = MAX_DIMENSION / maxSide.toFloat()
        val width = (source.width * scale).toInt()
        val height = (source.height * scale).toInt()
        return Bitmap.createScaledBitmap(source, width, height, true)
    }

    private fun compressToWebP(bitmap: Bitmap): ByteArray {
        val output = ByteArrayOutputStream()
        @Suppress("DEPRECATION")
        val format = Bitmap.CompressFormat.WEBP
        bitmap.compress(format, QUALITY, output)
        return output.toByteArray()
    }
}
