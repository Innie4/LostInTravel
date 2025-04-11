package com.example.lostintravel.domain.model

data class Destination(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val imageUrl: String,
    val rating: Double,
    val price: Double,
    val coordinates: Coordinates,
    val tags: List<String> = emptyList(),
    val amenities: List<Amenity> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val isFavorite: Boolean = false
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class Amenity(
    val id: String,
    val name: String,
    val icon: String
)

data class Review(
    val id: String,
    val userName: String,
    val rating: Double,
    val comment: String,
    val date: String
)