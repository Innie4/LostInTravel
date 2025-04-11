package com.example.lostintravel.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.lostintravel.domain.repository.DestinationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class CacheRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val destinationRepository: DestinationRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Timber.d("Starting cache refresh worker")
            
            // Refresh all destination categories
            destinationRepository.refreshDestinations()
            
            // We don't need to check the result here as we just want to attempt a refresh
            // If it fails, the app will continue to use the existing cache
            
            Timber.d("Cache refresh completed successfully")
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Cache refresh failed")
            Result.failure()
        }
    }
}