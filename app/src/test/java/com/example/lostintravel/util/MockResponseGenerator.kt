package com.example.lostintravel.util

import com.example.lostintravel.data.remote.dto.AmenityDto
import com.example.lostintravel.data.remote.dto.DestinationDto
import com.example.lostintravel.data.remote.dto.ReviewDto
import com.example.lostintravel.domain.model.Amenity
import com.example.lostintravel.domain.model.Coordinates
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.model.Review

object MockResponseGenerator {
    
    fun createMockDestinationDto(
        id: String = "1",
        name: String = "Test Destination",
        description: String = "A test destination",
        location: String = "Test Location",
        imageUrl: String = "https://example.com/image.jpg",
        rating: Double = 4.5,
        price: Double = 100.0,
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        tags: List<String> = listOf("test", "destination"),
        amenities: List<AmenityDto> = emptyList(),
        reviews: List<ReviewDto> = emptyList()
    ): DestinationDto {
        return DestinationDto(
            id = id,
            name = name,
            description = description,
            location = location,
            imageUrl = imageUrl,
            rating = rating,
            price = price,
            latitude = latitude,
            longitude = longitude,
            tags = tags,
            amenities = amenities,
            reviews = reviews
        )
    }
    
    fun createMockDestination(
        id: String = "1",
        name: String = "Test Destination",
        description: String = "A test destination",
        location: String = "Test Location",
        imageUrl: String = "https://example.com/image.jpg",
        rating: Double = 4.5,
        price: Double = 100.0,
        coordinates: Coordinates = Coordinates(0.0, 0.0),
        tags: List<String> = listOf("test", "destination"),
        amenities: List<Amenity> = emptyList(),
        reviews: List<Review> = emptyList(),
        isFavorite: Boolean = false
    ): Destination {
        return Destination(
            id = id,
            name = name,
            description = description,
            location = location,
            imageUrl = imageUrl,
            rating = rating,
            price = price,
            coordinates = coordinates,
            tags = tags,
            amenities = amenities,
            reviews = reviews,
            isFavorite = isFavorite
        )
    }
    
    fun createMockDestinationList(count: Int): List<DestinationDto> {
        return (1..count).map { 
            createMockDestinationDto(id = it.toString(), name = "Destination $it") 
        }
    }
}