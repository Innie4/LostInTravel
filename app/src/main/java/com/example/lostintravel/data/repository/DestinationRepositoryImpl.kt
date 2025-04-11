package com.example.lostintravel.data.repository

import com.example.lostintravel.data.local.LocalDataSource
import com.example.lostintravel.data.local.entity.CacheMetadata
import com.example.lostintravel.data.remote.RemoteDataSource
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.repository.DestinationRepository
import com.example.lostintravel.domain.util.Result
import com.example.lostintravel.util.NetworkConnectivityChecker
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class DestinationRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val networkChecker: NetworkConnectivityChecker
) : DestinationRepository {

    override suspend fun getDestinations(): Result<List<Destination>> {
        return try {
            // Check if cache is expired
            val isCacheExpired = localDataSource.isCacheExpired(CacheMetadata.KEY_DESTINATIONS)
            
            // If online and cache expired, fetch from network
            if (networkChecker.isNetworkAvailable() && isCacheExpired) {
                Timber.d("Fetching destinations from network")
                when (val remoteResult = remoteDataSource.getDestinations()) {
                    is Result.Success -> {
                        // Save to local cache
                        localDataSource.saveDestinations(remoteResult.data)
                        Result.Success(remoteResult.data)
                    }
                    is Result.Error -> {
                        // If network fetch fails, try to get from cache as fallback
                        val cachedDestinations = localDataSource.getAllDestinations()
                        if (cachedDestinations.isNotEmpty()) {
                            Timber.d("Network fetch failed, using cached data")
                            Result.Success(cachedDestinations)
                        } else {
                            Timber.e(remoteResult.exception, "Failed to fetch destinations")
                            remoteResult
                        }
                    }
                    is Result.Loading -> Result.Loading
                }
            } else {
                // Use cached data
                Timber.d("Using cached destinations")
                val cachedDestinations = localDataSource.getAllDestinations()
                
                if (cachedDestinations.isEmpty() && networkChecker.isNetworkAvailable()) {
                    // If cache is empty but we're online, fetch from network
                    Timber.d("Cache empty, fetching from network")
                    when (val remoteResult = remoteDataSource.getDestinations()) {
                        is Result.Success -> {
                            localDataSource.saveDestinations(remoteResult.data)
                            Result.Success(remoteResult.data)
                        }
                        is Result.Error -> {
                            Timber.e(remoteResult.exception, "Failed to fetch destinations")
                            remoteResult
                        }
                        is Result.Loading -> Result.Loading
                    }
                } else if (cachedDestinations.isEmpty()) {
                    // If cache is empty and offline
                    Result.Error(Exception("No cached data available and device is offline"))
                } else {
                    // Return cached data
                    Result.Success(cachedDestinations)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error in getDestinations")
            Result.Error(e)
        }
    }

    override suspend fun refreshDestinations(): Result<Unit> {
        return try {
            if (!networkChecker.isNetworkAvailable()) {
                return Result.Error(Exception("No internet connection"))
            }
            
            when (val remoteResult = remoteDataSource.getDestinations()) {
                is Result.Success -> {
                    localDataSource.saveDestinations(remoteResult.data)
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    Timber.e(remoteResult.exception, "Failed to refresh destinations")
                    remoteResult
                }
                is Result.Loading -> Result.Loading
            }
        } catch (e: Exception) {
            Timber.e(e, "Error in refreshDestinations")
            Result.Error(e)
        }
    }

    override suspend fun getPopularDestinations(): Result<List<Destination>> {
        return try {
            // Check if cache is expired
            val isCacheExpired = localDataSource.isCacheExpired(CacheMetadata.KEY_POPULAR_DESTINATIONS)
            
            // If online and cache expired, fetch from network
            if (networkChecker.isNetworkAvailable() && isCacheExpired) {
                Timber.d("Fetching popular destinations from network")
                when (val remoteResult = remoteDataSource.getPopularDestinations()) {
                    is Result.Success -> {
                        // Save to local cache
                        localDataSource.savePopularDestinations(remoteResult.data)
                        Result.Success(remoteResult.data)
                    }
                    is Result.Error -> {
                        // If network fetch fails, try to get from cache as fallback
                        val cachedDestinations = localDataSource.getPopularDestinations()
                        if (cachedDestinations.isNotEmpty()) {
                            Timber.d("Network fetch failed, using cached popular destinations")
                            Result.Success(cachedDestinations)
                        } else {
                            Timber.e(remoteResult.exception, "Failed to fetch popular destinations")
                            remoteResult
                        }
                    }
                    is Result.Loading -> Result.Loading
                }
            } else {
                // Use cached data
                Timber.d("Using cached popular destinations")
                val cachedDestinations = localDataSource.getPopularDestinations()
                
                if (cachedDestinations.isEmpty() && networkChecker.isNetworkAvailable()) {
                    // If cache is empty but we're online, fetch from network
                    Timber.d("Cache empty, fetching popular destinations from network")
                    when (val remoteResult = remoteDataSource.getPopularDestinations()) {
                        is Result.Success -> {
                            localDataSource.savePopularDestinations(remoteResult.data)
                            Result.Success(remoteResult.data)
                        }
                        is Result.Error -> {
                            Timber.e(remoteResult.exception, "Failed to fetch popular destinations")
                            remoteResult
                        }
                        is Result.Loading -> Result.Loading
                    }
                } else if (cachedDestinations.isEmpty()) {
                    // If cache is empty and offline
                    Result.Error(Exception("No cached popular destinations available and device is offline"))
                } else {
                    // Return cached data
                    Result.Success(cachedDestinations)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error in getPopularDestinations")
            Result.Error(e)
        }
    }

    override suspend fun getRecommendedDestinations(): Result<List<Destination>> {
        return try {
            // Check if cache is expired
            val isCacheExpired = localDataSource.isCacheExpired(CacheMetadata.KEY_RECOMMENDED_DESTINATIONS)
            
            // If online and cache expired, fetch from network
            if (networkChecker.isNetworkAvailable() && isCacheExpired) {
                Timber.d("Fetching recommended destinations from network")
                when (val remoteResult = remoteDataSource.getRecommendedDestinations()) {
                    is Result.Success -> {
                        // Save to local cache
                        localDataSource.saveRecommendedDestinations(remoteResult.data)
                        Result.Success(remoteResult.data)
                    }
                    is Result.Error -> {
                        // If network fetch fails, try to get from cache as fallback
                        val cachedDestinations = localDataSource.getRecommendedDestinations()
                        if (cachedDestinations.isNotEmpty()) {
                            Timber.d("Network fetch failed, using cached recommended destinations")
                            Result.Success(cachedDestinations)
                        } else {
                            Timber.e(remoteResult.exception, "Failed to fetch recommended destinations")
                            remoteResult
                        }
                    }
                    is Result.Loading -> Result.Loading
                }
            } else {
                // Use cached data
                Timber.d("Using cached recommended destinations")
                val cachedDestinations = localDataSource.getRecommendedDestinations()
                
                if (cachedDestinations.isEmpty() && networkChecker.isNetworkAvailable()) {
                    // If cache is empty but we're online, fetch from network
                    Timber.d("Cache empty, fetching recommended destinations from network")
                    when (val remoteResult = remoteDataSource.getRecommendedDestinations()) {
                        is Result.Success -> {
                            localDataSource.saveRecommendedDestinations(remoteResult.data)
                            Result.Success(remoteResult.data)
                        }
                        is Result.Error -> {
                            Timber.e(remoteResult.exception, "Failed to fetch recommended destinations")
                            remoteResult
                        }
                        is Result.Loading -> Result.Loading
                    }
                } else if (cachedDestinations.isEmpty()) {
                    // If cache is empty and offline
                    Result.Error(Exception("No cached recommended destinations available and device is offline"))
                } else {
                    // Return cached data
                    Result.Success(cachedDestinations)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error in getRecommendedDestinations")
            Result.Error(e)
        }
    }

    override suspend fun getFeaturedDestinations(): Result<List<Destination>> {
        // Similar implementation as getPopularDestinations and getRecommendedDestinations
        // Omitted for brevity
        return try {
            val isCacheExpired = localDataSource.isCacheExpired(CacheMetadata.KEY_FEATURED_DESTINATIONS)
            
            if (networkChecker.isNetworkAvailable() && isCacheExpired) {
                when (val remoteResult = remoteDataSource.getFeaturedDestinations()) {
                    is Result.Success -> {
                        localDataSource.saveFeaturedDestinations(remoteResult.data)
                        Result.Success(remoteResult.data)
                    }
                    is Result.Error -> {
                        val cachedDestinations = localDataSource.getFeaturedDestinations()
                        if (cachedDestinations.isNotEmpty()) {
                            Result.Success(cachedDestinations)
                        } else {
                            remoteResult
                        }
                    }
                    is Result.Loading -> Result.Loading
                }
            } else {
                val cachedDestinations = localDataSource.getFeaturedDestinations()
                
                if (cachedDestinations.isEmpty() && networkChecker.isNetworkAvailable()) {
                    when (val remoteResult = remoteDataSource.getFeaturedDestinations()) {
                        is Result.Success -> {
                            localDataSource.saveFeaturedDestinations(remoteResult.data)
                            Result.Success(remoteResult.data)
                        }
                        is Result.Error -> remoteResult
                        is Result.Loading -> Result.Loading
                    }
                } else if (cachedDestinations.isEmpty()) {
                    Result.Error(Exception("No cached featured destinations available and device is offline"))
                } else {
                    Result.Success(cachedDestinations)
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getFavoriteDestinations(): Flow<List<Destination>> {
        return localDataSource.observeFavoriteDestinations()
    }

    override suspend fun toggleFavorite(destinationId: String): Result<Unit> {
        return try {
            val destination = localDataSource.getDestinationById(destinationId)
            if (destination != null) {
                val newFavoriteStatus = !destination.isFavorite
                localDataSource.updateFavoriteStatus(destinationId, newFavoriteStatus)
                
                // If online, sync with remote
                if (networkChecker.isNetworkAvailable()) {
                    try {
                        remoteDataSource.updateFavoriteStatus(destinationId, newFavoriteStatus)
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to sync favorite status with remote")
                        // We don't return an error here as the local update was successful
                    }
                }
                
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Destination not found"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error in toggleFavorite")
            Result.Error(e)
        }
    }

    override suspend fun searchDestinations(query: String): Result<List<Destination>> {
        return try {
            // First try local search
            val localResults = localDataSource.searchDestinations(query)
            
            // If online and local results are empty or limited, try remote search
            if (networkChecker.isNetworkAvailable() && localResults.size < 5) {
                Timber.d("Searching destinations from network")
                when (val remoteResult = remoteDataSource.searchDestinations(query)) {
                    is Result.Success -> {
                        // Save results to cache for future offline access
                        localDataSource.saveDestinations(remoteResult.data)
                        Result.Success(remoteResult.data)
                    }
                    is Result.Error -> {
                        // If remote search fails but we have local results, return those
                        if (localResults.isNotEmpty()) {
                            Timber.d("Remote search failed, using local search results")
                            Result.Success(localResults)
                        } else {
                            Timber.e(remoteResult.exception, "Failed to search destinations")
                            remoteResult
                        }
                    }
                    is Result.Loading -> Result.Loading
                }
            } else {
                // Return local results
                Result.Success(localResults)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error in searchDestinations")
            Result.Error(e)
        }
    }

    override suspend fun clearCache(): Result<Unit> {
        return try {
            localDataSource.clearCache()
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error in clearCache")
            Result.Error(e)
        }
    }
}