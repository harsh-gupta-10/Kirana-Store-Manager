package com.kiranstore.manager.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.kiranstore.manager.util.ImageCompressor
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class SupabaseStorageManager @Inject constructor(
    private val client: SupabaseClient
) {
    companion object {
        private const val BUCKET_NAME = "store-images"
    }

    suspend fun uploadImage(
        context: Context,
        imageUri: Uri,
        folder: String = "uploads"
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
                ?: return@withContext Result.failure(Exception("Cannot open image"))

            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val compressedBytes = ImageCompressor.compressToWebP(
                bitmap = originalBitmap,
                quality = 80
            )
            originalBitmap.recycle()

            val fileName = "$folder/${UUID.randomUUID()}.webp"
            val bucket = client.storage[BUCKET_NAME]
            bucket.upload(fileName, compressedBytes)

            val publicUrl = bucket.publicUrl(fileName)
            Result.success(publicUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadBitmap(
        bitmap: Bitmap,
        folder: String = "uploads"
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val compressedBytes = ImageCompressor.compressToWebP(
                bitmap = bitmap,
                quality = 80
            )

            val fileName = "$folder/${UUID.randomUUID()}.webp"
            val bucket = client.storage[BUCKET_NAME]
            bucket.upload(fileName, compressedBytes)

            val publicUrl = bucket.publicUrl(fileName)
            Result.success(publicUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteImage(filePath: String): Result<Unit> {
        return try {
            val bucket = client.storage[BUCKET_NAME]
            bucket.delete(filePath)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
