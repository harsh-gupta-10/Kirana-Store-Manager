package com.kiranstore.manager.data.remote

import com.kiranstore.manager.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(GoTrue)
            install(Postgrest)
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseAuthManager(client: SupabaseClient): SupabaseAuthManager {
        return SupabaseAuthManager(client)
    }

    @Provides
    @Singleton
    fun provideSupabaseSyncManager(client: SupabaseClient): SupabaseSyncManager {
        return SupabaseSyncManager(client)
    }

    @Provides
    @Singleton
    fun provideSupabaseStorageManager(client: SupabaseClient): SupabaseStorageManager {
        return SupabaseStorageManager(client)
    }
}
