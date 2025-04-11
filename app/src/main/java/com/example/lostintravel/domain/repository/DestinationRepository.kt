package com.example.lostintravel.domain.repository

import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface DestinationRepository {
    suspend fun getDestinations(): Result<List<Destination>>
    suspend fun refreshDestinations(): Result<Unit>
    suspend fun getPopularDestinations(): Result<List<Destination>>
    suspend fun getRecommendedDestinations(): Result<List<Destination>>
    suspend fun getFeaturedDestinations(): Result<List<Destination>>
    fun getFavoriteDestinations(): Flow<List<Destination>>
    suspend fun toggleFavorite(destinationId: String): Result<Unit>
    suspend fun searchDestinations(query: String): Result<List<Destination>>
    suspend fun clearCache(): Result<Unit>
    
    // Add these methods for the destination detail screen
    suspend fun getDestinationById(id: String): Result<Destination>
    suspend fun refreshDestination(id: String): Result<Unit>
}