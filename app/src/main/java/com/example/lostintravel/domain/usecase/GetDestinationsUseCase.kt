package com.example.lostintravel.domain.usecase

import com.example.lostintravel.data.remote.util.Result
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.repository.DestinationRepository
import javax.inject.Inject

class GetDestinationsUseCase @Inject constructor(
    private val destinationRepository: DestinationRepository
) {
    suspend operator fun invoke(): Result<List<Destination>> {
        return destinationRepository.getDestinations()
    }
}