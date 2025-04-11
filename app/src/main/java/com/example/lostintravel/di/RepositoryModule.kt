package com.example.lostintravel.di

import com.example.lostintravel.data.local.LocalDataSource
import com.example.lostintravel.data.remote.RemoteDataSource
import com.example.lostintravel.data.repository.DestinationRepositoryImpl
import com.example.lostintravel.domain.repository.DestinationRepository
import com.example.lostintravel.util.NetworkConnectivityChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDestinationRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        networkChecker: NetworkConnectivityChecker
    ): DestinationRepository {
        return DestinationRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            networkChecker = networkChecker
        )
    }
}