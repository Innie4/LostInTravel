package com.example.lostintravel.domain.usecase

import com.example.lostintravel.data.remote.util.Result
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.repository.DestinationRepository
import javax.inject.Inject

class SearchDestinationsUseCase @Inject constructor(
    private val destinationRepository: DestinationRepository
) {
    suspend operator fun invoke(query: String): Result<List<Destination>> {
        return if (query.isBlank()) {
            destinationRepository.getDestinations()
        } else {
            destinationRepository.searchDestinations(query)
        }
    }
}