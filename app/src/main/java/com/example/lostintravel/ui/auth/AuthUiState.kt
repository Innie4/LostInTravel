package com.example.lostintravel.ui.auth

import com.example.lostintravel.domain.model.AuthState

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    data class Authenticated(val authState: AuthState) : AuthUiState()
    data class Unauthenticated(val message: String? = null) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}