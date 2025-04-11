package com.example.lostintravel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.lostintravel.data.local.entity.DestinationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DestinationDao {
    @Query("SELECT * FROM destinations")
    fun getAllDestinations(): Flow<List<DestinationEntity>>

    @Query("SELECT * FROM destinations")
    suspend fun getAllDestinationsList(): List<DestinationEntity>

    @Query("SELECT * FROM destinations WHERE id = :id")
    suspend fun getDestinationById(id: String): DestinationEntity?

    @Query("SELECT * FROM destinations WHERE isPopular = 1")
    suspend fun getPopularDestinations(): List<DestinationEntity>

    @Query("SELECT * FROM destinations WHERE isRecommended = 1")
    suspend fun getRecommendedDestinations(): List<DestinationEntity>

    @Query("SELECT * FROM destinations WHERE isFeatured = 1")
    suspend fun getFeaturedDestinations(): List<DestinationEntity>

    @Query("SELECT * FROM destinations WHERE isFavorite = 1")
    fun getFavoriteDestinations(): Flow<List<DestinationEntity>>

    @Query("SELECT * FROM destinations WHERE name LIKE '%' || :query || '%' OR city LIKE '%' || :query || '%' OR country LIKE '%' || :query || '%'")
    suspend fun searchDestinations(query: String): List<DestinationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDestinations(destinations: List<DestinationEntity>)

    @Query("UPDATE destinations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("DELETE FROM destinations WHERE id = :id")
    suspend fun deleteDestination(id: String)

    @Query("DELETE FROM destinations")
    suspend fun clearAllDestinations()

    @Transaction
    suspend fun refreshDestinations(destinations: List<DestinationEntity>) {
        clearAllDestinations()
        insertDestinations(destinations)
    }

    @Transaction
    suspend fun refreshPopularDestinations(destinations: List<DestinationEntity>) {
        // First, reset all popular flags
        resetPopularDestinations()
        // Then insert or update the new popular destinations
        insertDestinations(destinations)
    }

    @Query("UPDATE destinations SET isPopular = 0")
    suspend fun resetPopularDestinations()

    @Transaction
    suspend fun refreshRecommendedDestinations(destinations: List<DestinationEntity>) {
        // First, reset all recommended flags
        resetRecommendedDestinations()
        // Then insert or update the new recommended destinations
        insertDestinations(destinations)
    }

    @Query("UPDATE destinations SET isRecommended = 0")
    suspend fun resetRecommendedDestinations()

    @Transaction
    suspend fun refreshFeaturedDestinations(destinations: List<DestinationEntity>) {
        // First, reset all featured flags
        resetFeaturedDestinations()
        // Then insert or update the new featured destinations
        insertDestinations(destinations)
    }

    @Query("UPDATE destinations SET isFeatured = 0")
    suspend fun resetFeaturedDestinations()
}