package com.example.lostintravel.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val photoUrl: String? = null,
    val isEmailVerified: Boolean = false
)