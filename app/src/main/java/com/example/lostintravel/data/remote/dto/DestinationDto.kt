package com.example.lostintravel.data.remote.dto

import com.example.lostintravel.domain.model.Destination
import com.google.gson.annotations.SerializedName

data class DestinationDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("location") val location: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("price") val price: Double,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("tags") val tags: List<String> = emptyList(),
    @SerializedName("amenities") val amenities: List<AmenityDto> = emptyList(),
    @SerializedName("reviews") val reviews: List<ReviewDto> = emptyList()
)

data class AmenityDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: String
)

data class ReviewDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("comment") val comment: String,
    @SerializedName("date") val date: String
) {
    fun toDomain(): Destination {
        return Destination(
            id = id,
            name = name,
            description = description,
            location = location,
            imageUrl = imageUrl,
            rating = rating,
            price = price,
            tags = tags
        )
    }
}