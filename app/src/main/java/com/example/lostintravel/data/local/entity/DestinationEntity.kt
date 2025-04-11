package com.example.lostintravel.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lostintravel.domain.model.Destination
import java.util.Date

@Entity(tableName = "destinations")
data class DestinationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val city: String,
    val country: String,
    val description: String,
    val imageUrl: String,
    val rating: Double,
    val priceLevel: String,
    val isFavorite: Boolean = false,
    val isPopular: Boolean = false,
    val isRecommended: Boolean = false,
    val isFeatured: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toDestination(): Destination {
        return Destination(
            id = id,
            name = name,
            city = city,
            country = country,
            description = description,
            imageUrl = imageUrl,
            rating = rating,
            priceLevel = priceLevel,
            isFavorite = isFavorite
        )
    }
}

fun Destination.toEntity(
    isPopular: Boolean = false,
    isRecommended: Boolean = false,
    isFeatured: Boolean = false
): DestinationEntity {
    return DestinationEntity(
        id = id,
        name = name,
        city = city,
        country = country,
        description = description,
        imageUrl = imageUrl,
        rating = rating,
        priceLevel = priceLevel,
        isFavorite = isFavorite,
        isPopular = isPopular,
        isRecommended = isRecommended,
        isFeatured = isFeatured
    )
}