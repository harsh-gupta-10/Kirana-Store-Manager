package com.kiranstore.manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.services.auth.AuthResult
import com.kiranstore.manager.services.auth.SupabaseAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userId: String = "",
    val error: String = "",
    val isNewUser: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: SupabaseAuthService
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() = viewModelScope.launch {
        val restored = authService.restoreSession()
        if (restored) {
            _state.update { it.copy(isLoggedIn = true, userId = authService.currentUser?.id ?: "") }
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = "") }
        when (val result = authService.login(email, password)) {
            is AuthResult.Success -> _state.update { it.copy(isLoading = false, isLoggedIn = true, userId = result.userId) }
            is AuthResult.Error   -> _state.update { it.copy(isLoading = false, error = result.message) }
        }
    }

    fun signUp(email: String, password: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = "") }
        when (val result = authService.signUp(email, password)) {
            is AuthResult.Success -> _state.update { it.copy(isLoading = false, isLoggedIn = true, isNewUser = true, userId = result.userId) }
            is AuthResult.Error   -> _state.update { it.copy(isLoading = false, error = result.message) }
        }
    }

    fun logout() = viewModelScope.launch {
        authService.logout()
        _state.value = AuthUiState()
    }

    fun clearError() = _state.update { it.copy(error = "") }
}
