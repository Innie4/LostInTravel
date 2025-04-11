package com.example.lostintravel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lostintravel.data.local.entity.CacheMetadata

@Dao
interface CacheMetadataDao {
    @Query("SELECT * FROM cache_metadata WHERE key = :key")
    suspend fun getCacheMetadata(key: String): CacheMetadata?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCacheMetadata(cacheMetadata: CacheMetadata)

    @Query("UPDATE cache_metadata SET lastRefreshed = :timestamp WHERE key = :key")
    suspend fun updateLastRefreshed(key: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM cache_metadata WHERE key = :key")
    suspend fun deleteCacheMetadata(key: String)
}