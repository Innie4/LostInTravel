package com.example.lostintravel.domain.model

data class AuthState(
    val isAuthenticated: Boolean = false,
    val userId: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null
)