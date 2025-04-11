package com.example.lostintravel.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_metadata")
data class CacheMetadata(
    @PrimaryKey
    val key: String,
    val lastRefreshed: Long = System.currentTimeMillis(),
    val expirationTimeMillis: Long = DEFAULT_CACHE_EXPIRATION
) {
    companion object {
        const val DEFAULT_CACHE_EXPIRATION = 24 * 60 * 60 * 1000L // 24 hours in milliseconds
        const val KEY_DESTINATIONS = "destinations"
        const val KEY_POPULAR_DESTINATIONS = "popular_destinations"
        const val KEY_RECOMMENDED_DESTINATIONS = "recommended_destinations"
        const val KEY_FEATURED_DESTINATIONS = "featured_destinations"
    }

    fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime - lastRefreshed > expirationTimeMillis
    }
}