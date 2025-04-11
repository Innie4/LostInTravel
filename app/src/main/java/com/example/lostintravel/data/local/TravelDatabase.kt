package com.example.lostintravel.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lostintravel.data.local.dao.CacheMetadataDao
import com.example.lostintravel.data.local.dao.DestinationDao
import com.example.lostintravel.data.local.entity.CacheMetadata
import com.example.lostintravel.data.local.entity.DestinationEntity

@Database(
    entities = [DestinationEntity::class, CacheMetadata::class],
    version = 1,
    exportSchema = false
)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun destinationDao(): DestinationDao
    abstract fun cacheMetadataDao(): CacheMetadataDao

    companion object {
        private const val DATABASE_NAME = "travel_database"

        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getInstance(context: Context): TravelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}