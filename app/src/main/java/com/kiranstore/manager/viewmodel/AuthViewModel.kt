package com.kiranstore.manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiranstore.manager.data.remote.AuthState
import com.kiranstore.manager.data.remote.SupabaseAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: SupabaseAuthManager
) : ViewModel() {

    val authState: StateFlow<AuthState> = authManager.authState

    init {
        viewModelScope.launch {
            authManager.checkSession()
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authManager.signIn(email, password)
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authManager.signUp(email, password)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authManager.signOut()
        }
    }
}
