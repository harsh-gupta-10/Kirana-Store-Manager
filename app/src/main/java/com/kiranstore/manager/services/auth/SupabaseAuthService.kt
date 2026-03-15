package com.kiranstore.manager.services.auth

import com.kiranstore.manager.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

sealed class AuthResult {
    data class Success(val userId: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

@Singleton
class SupabaseAuthService @Inject constructor() {

    private val supabase = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Auth)
    }

    // ── Current session ────────────────────────────────────
    val currentUser: UserInfo?
        get() = supabase.auth.currentUserOrNull()

    val isLoggedIn: Boolean
        get() = currentUser != null

    // ── Sign Up ─────────────────────────────────────────────
    suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val uid = supabase.auth.currentUserOrNull()?.id ?: ""
            AuthResult.Success(uid)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Signup failed")
        }
    }

    // ── Login ──────────────────────────────────────────────
    suspend fun login(email: String, password: String): AuthResult {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val uid = supabase.auth.currentUserOrNull()?.id ?: ""
            AuthResult.Success(uid)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    // ── Logout ─────────────────────────────────────────────
    suspend fun logout(): AuthResult {
        return try {
            supabase.auth.signOut()
            AuthResult.Success("")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Logout failed")
        }
    }

    // ── Restore session ────────────────────────────────────
    suspend fun restoreSession(): Boolean {
        return try {
            // In Supabase KT 3.x, session management is often handled automatically if a storage is provided.
            // But manually loading is still supported via the sessionManager.
            currentUser != null
        } catch (e: Exception) {
            false
        }
    }
}
