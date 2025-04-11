package com.example.lostintravel.data.local.entity

import com.example.lostintravel.domain.model.User

data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val favoriteDestinations: List<String> = emptyList(),
    val visitedDestinations: List<String> = emptyList()
) {
    fun toDomain(): User {
        return User(
            id = id,
            name = name,
            email = email,
            profilePictureUrl = profilePictureUrl,
            favoriteDestinations = favoriteDestinations,
            visitedDestinations = visitedDestinations
        )
    }
}