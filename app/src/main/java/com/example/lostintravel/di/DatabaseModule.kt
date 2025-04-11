package com.example.lostintravel.di

import android.content.Context
import com.example.lostintravel.data.local.LocalDataSource
import com.example.lostintravel.data.local.TravelDatabase
import com.example.lostintravel.data.local.dao.CacheMetadataDao
import com.example.lostintravel.data.local.dao.DestinationDao
import com.example.lostintravel.util.NetworkConnectivityChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTravelDatabase(@ApplicationContext context: Context): TravelDatabase {
        return TravelDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideDestinationDao(database: TravelDatabase): DestinationDao {
        return database.destinationDao()
    }

    @Provides
    @Singleton
    fun provideCacheMetadataDao(database: TravelDatabase): CacheMetadataDao {
        return database.cacheMetadataDao()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        destinationDao: DestinationDao,
        cacheMetadataDao: CacheMetadataDao
    ): LocalDataSource {
        return LocalDataSource(destinationDao, cacheMetadataDao)
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityChecker(
        @ApplicationContext context: Context
    ): NetworkConnectivityChecker {
        return NetworkConnectivityChecker(context)
    }
}