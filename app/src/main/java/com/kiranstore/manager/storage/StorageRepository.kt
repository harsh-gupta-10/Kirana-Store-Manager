package com.kiranstore.manager.storage

import io.github.jan.supabase.storage.Storage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor(
    private val storage: Storage
) {
    companion object {
        private const val BUCKET_NAME = "shop-images"
    }

    suspend fun uploadImage(fileName: String, data: ByteArray): String {
        val bucket = storage.from(BUCKET_NAME)
        bucket.upload(fileName, data, upsert = true)
        return bucket.publicUrl(fileName)
    }
}
