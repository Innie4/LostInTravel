package com.example.lostintravel.data.local

import com.example.lostintravel.data.local.dao.CacheMetadataDao
import com.example.lostintravel.data.local.dao.DestinationDao
import com.example.lostintravel.data.local.entity.CacheMetadata
import com.example.lostintravel.data.local.entity.DestinationEntity
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.data.local.entity.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val destinationDao: DestinationDao,
    private val cacheMetadataDao: CacheMetadataDao
) {
    // Destinations
    suspend fun getAllDestinations(): List<Destination> {
        return destinationDao.getAllDestinationsList().map { it.toDestination() }
    }

    fun observeAllDestinations(): Flow<List<Destination>> {
        return destinationDao.getAllDestinations().map { entities ->
            entities.map { it.toDestination() }
        }
    }

    suspend fun getDestinationById(id: String): Destination? {
        return destinationDao.getDestinationById(id)?.toDestination()
    }

    suspend fun getPopularDestinations(): List<Destination> {
        return destinationDao.getPopularDestinations().map { it.toDestination() }
    }

    suspend fun getRecommendedDestinations(): List<Destination> {
        return destinationDao.getRecommendedDestinations().map { it.toDestination() }
    }

    suspend fun getFeaturedDestinations(): List<Destination> {
        return destinationDao.getFeaturedDestinations().map { it.toDestination() }
    }

    fun observeFavoriteDestinations(): Flow<List<Destination>> {
        return destinationDao.getFavoriteDestinations().map { entities ->
            entities.map { it.toDestination() }
        }
    }

    suspend fun searchDestinations(query: String): List<Destination> {
        return destinationDao.searchDestinations(query).map { it.toDestination() }
    }

    suspend fun saveDestinations(destinations: List<Destination>) {
        val entities = destinations.map { it.toEntity() }
        destinationDao.insertDestinations(entities)
        updateCacheMetadata(CacheMetadata.KEY_DESTINATIONS)
    }

    suspend fun savePopularDestinations(destinations: List<Destination>) {
        val entities = destinations.map { it.toEntity(isPopular = true) }
        destinationDao.refreshPopularDestinations(entities)
        updateCacheMetadata(CacheMetadata.KEY_POPULAR_DESTINATIONS)
    }

    suspend fun saveRecommendedDestinations(destinations: List<Destination>) {
        val entities = destinations.map { it.toEntity(isRecommended = true) }
        destinationDao.refreshRecommendedDestinations(entities)
        updateCacheMetadata(CacheMetadata.KEY_RECOMMENDED_DESTINATIONS)
    }

    suspend fun saveFeaturedDestinations(destinations: List<Destination>) {
        val entities = destinations.map { it.toEntity(isFeatured = true) }
        destinationDao.refreshFeaturedDestinations(entities)
        updateCacheMetadata(CacheMetadata.KEY_FEATURED_DESTINATIONS)
    }

    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
        destinationDao.updateFavoriteStatus(id, isFavorite)
    }

    // Cache metadata
    suspend fun isCacheExpired(key: String): Boolean {
        val metadata = cacheMetadataDao.getCacheMetadata(key)
        return metadata?.isExpired() ?: true
    }

    private suspend fun updateCacheMetadata(key: String) {
        val metadata = CacheMetadata(key = key)
        cacheMetadataDao.insertCacheMetadata(metadata)
    }

    suspend fun clearCache() {
        destinationDao.clearAllDestinations()
        // Clear all cache metadata
        cacheMetadataDao.deleteCacheMetadata(CacheMetadata.KEY_DESTINATIONS)
        cacheMetadataDao.deleteCacheMetadata(CacheMetadata.KEY_POPULAR_DESTINATIONS)
        cacheMetadataDao.deleteCacheMetadata(CacheMetadata.KEY_RECOMMENDED_DESTINATIONS)
        cacheMetadataDao.deleteCacheMetadata(CacheMetadata.KEY_FEATURED_DESTINATIONS)
    }
}