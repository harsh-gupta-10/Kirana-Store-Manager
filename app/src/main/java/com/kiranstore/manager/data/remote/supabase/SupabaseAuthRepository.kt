package com.kiranstore.manager.data.remote.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.currentSessionOrNull
import io.github.jan.supabase.auth.signInWith
import io.github.jan.supabase.auth.signOut
import io.github.jan.supabase.auth.signUpWith
import io.github.jan.supabase.auth.session.Session
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseAuthRepository @Inject constructor(
    private val client: SupabaseClient?
) {

    private fun requireClient(): SupabaseClient =
        client ?: error("Supabase credentials are missing. Set SUPABASE_URL and SUPABASE_ANON_KEY.")

    suspend fun signIn(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            requireClient().auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }.map { }
    }

    suspend fun signUp(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            requireClient().auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        }.map { }
    }

    suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { requireClient().auth.signOut() }.map { }
    }

    suspend fun currentSession(): Session? = withContext(Dispatchers.IO) {
        client?.auth?.currentSessionOrNull()
    }
}
