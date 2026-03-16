package com.kiranstore.manager.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.remote.supabase.SupabaseAuthRepository
import com.kiranstore.manager.data.remote.supabase.SupabaseStorageRepository
import com.kiranstore.manager.data.remote.supabase.SupabaseSyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val email: String? = null
)

data class SyncUiState(
    val syncing: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

data class UploadUiState(
    val uploading: Boolean = false,
    val url: String? = null,
    val error: String? = null
)

@HiltViewModel
class CloudViewModel @Inject constructor(
    private val authRepository: SupabaseAuthRepository,
    private val syncRepository: SupabaseSyncRepository,
    private val storageRepository: SupabaseStorageRepository
) : ViewModel() {

    private val _auth = MutableStateFlow(AuthUiState())
    val auth: StateFlow<AuthUiState> = _auth

    private val _sync = MutableStateFlow(SyncUiState())
    val sync: StateFlow<SyncUiState> = _sync

    private val _upload = MutableStateFlow(UploadUiState())
    val upload: StateFlow<UploadUiState> = _upload

    init {
        refreshSession()
    }

    fun refreshSession() {
        viewModelScope.launch {
            val session = authRepository.currentSession()
            _auth.update {
                it.copy(
                    email = session?.user?.email,
                    message = if (session != null) "Signed in" else "Not signed in"
                )
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _auth.update { it.copy(loading = true, message = null, error = null) }
            val result = authRepository.signIn(email.trim(), password)
            _auth.update {
                if (result.isSuccess) {
                    it.copy(
                        loading = false,
                        email = email,
                        message = "Signed in",
                        error = null
                    )
                } else {
                    it.copy(loading = false, error = result.exceptionOrNull()?.message ?: "Login failed")
                }
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _auth.update { it.copy(loading = true, message = null, error = null) }
            val result = authRepository.signUp(email.trim(), password)
            _auth.update {
                if (result.isSuccess) {
                    it.copy(
                        loading = false,
                        email = email,
                        message = "Account created. Verify email then sign in.",
                        error = null
                    )
                } else {
                    it.copy(loading = false, error = result.exceptionOrNull()?.message ?: "Signup failed")
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _auth.update { it.copy(loading = true, message = null, error = null) }
            val result = authRepository.signOut()
            _auth.update {
                if (result.isSuccess) {
                    it.copy(loading = false, email = null, message = "Signed out", error = null)
                } else {
                    it.copy(loading = false, error = result.exceptionOrNull()?.message ?: "Sign out failed")
                }
            }
        }
    }

    fun syncNow() {
        viewModelScope.launch {
            _sync.update { it.copy(syncing = true, message = null, error = null) }
            val result = syncRepository.syncAll()
            _sync.update {
                if (result.isSuccess) {
                    it.copy(syncing = false, message = "Sync complete", error = null)
                } else {
                    it.copy(syncing = false, error = result.exceptionOrNull()?.message ?: "Sync failed")
                }
            }
        }
    }

    fun uploadCustomerImage(customerId: Long, uri: Uri, context: Context) {
        viewModelScope.launch {
            _upload.update { it.copy(uploading = true, url = null, error = null) }
            val result = storageRepository.uploadCustomerImage(customerId, uri, context)
            _upload.update {
                if (result.isSuccess) {
                    it.copy(uploading = false, url = result.getOrNull(), error = null)
                } else {
                    it.copy(uploading = false, error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun clearUploadState() {
        _upload.value = UploadUiState()
    }
}
