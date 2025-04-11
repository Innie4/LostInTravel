package com.example.lostintravel.data.mapper

import com.example.lostintravel.data.remote.dto.AmenityDto
import com.example.lostintravel.data.remote.dto.DestinationDto
import com.example.lostintravel.data.remote.dto.ReviewDto
import com.example.lostintravel.domain.model.Amenity
import com.example.lostintravel.domain.model.Coordinates
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.model.Review
import javax.inject.Inject

class DestinationMapper @Inject constructor() {
    
    fun mapToDomain(dto: DestinationDto, isFavorite: Boolean = false): Destination {
        return Destination(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            location = dto.location,
            imageUrl = dto.imageUrl,
            rating = dto.rating,
            price = dto.price,
            coordinates = Coordinates(
                latitude = dto.latitude,
                longitude = dto.longitude
            ),
            tags = dto.tags,
            amenities = dto.amenities.map { mapAmenityToDomain(it) },
            reviews = dto.reviews.map { mapReviewToDomain(it) },
            isFavorite = isFavorite
        )
    }
    
    private fun mapAmenityToDomain(dto: AmenityDto): Amenity {
        return Amenity(
            id = dto.id,
            name = dto.name,
            icon = dto.icon
        )
    }
    
    private fun mapReviewToDomain(dto: ReviewDto): Review {
        return Review(
            id = dto.id,
            userName = dto.userName,
            rating = dto.rating,
            comment = dto.comment,
            date = dto.date
        )
    }
}