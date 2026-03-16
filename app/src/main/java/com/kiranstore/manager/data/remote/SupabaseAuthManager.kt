package com.kiranstore.manager.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val userEmail: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class SupabaseAuthManager @Inject constructor(
    private val client: SupabaseClient
) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val isAuthenticated: Boolean
        get() = _authState.value is AuthState.Authenticated

    suspend fun signUp(email: String, password: String): Result<Unit> {
        _authState.value = AuthState.Loading
        return try {
            client.gotrue.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            _authState.value = AuthState.Authenticated(email)
            Result.success(Unit)
        } catch (e: Exception) {
            val msg = e.message ?: "Sign up failed"
            _authState.value = AuthState.Error(msg)
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<Unit> {
        _authState.value = AuthState.Loading
        return try {
            client.gotrue.loginWith(Email) {
                this.email = email
                this.password = password
            }
            _authState.value = AuthState.Authenticated(email)
            Result.success(Unit)
        } catch (e: Exception) {
            val msg = e.message ?: "Sign in failed"
            _authState.value = AuthState.Error(msg)
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            client.gotrue.logout()
            _authState.value = AuthState.Idle
            Result.success(Unit)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Sign out failed")
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): UserInfo? {
        return try {
            client.gotrue.retrieveUserForCurrentSession()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun checkSession() {
        try {
            val user = client.gotrue.retrieveUserForCurrentSession()
            val email = user.email
            if (email != null) {
                _authState.value = AuthState.Authenticated(email)
            } else {
                _authState.value = AuthState.Idle
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Idle
        }
    }
}
