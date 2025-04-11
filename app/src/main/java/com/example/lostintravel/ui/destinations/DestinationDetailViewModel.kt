package com.example.lostintravel.ui.destinations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.repository.DestinationRepository
import com.example.lostintravel.domain.util.Result
import com.example.lostintravel.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DestinationDetailViewModel @Inject constructor(
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Destination>>(UiState.Loading)
    val uiState: StateFlow<UiState<Destination>> = _uiState.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    private var currentDestinationId: String? = null
    
    fun loadDestination(destinationId: String) {
        currentDestinationId = destinationId
        _uiState.value = UiState.Loading
        
        viewModelScope.launch {
            try {
                val result = destinationRepository.getDestinationById(destinationId)
                when (result) {
                    is Result.Success -> {
                        _uiState.value = UiState.Success(result.data)
                    }
                    is Result.Error -> {
                        _uiState.value = UiState.Error(
                            result.exception.message ?: "Failed to load destination"
                        )
                        Timber.e(result.exception, "Error loading destination")
                    }
                    is Result.Loading -> {
                        _uiState.value = UiState.Loading
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unexpected error occurred")
                Timber.e(e, "Error loading destination")
            }
        }
    }
    
    fun refreshDestination() {
        currentDestinationId?.let { destinationId ->
            _isRefreshing.value = true
            
            viewModelScope.launch {
                try {
                    destinationRepository.refreshDestination(destinationId)
                    loadDestination(destinationId)
                } catch (e: Exception) {
                    Timber.e(e, "Error refreshing destination")
                } finally {
                    _isRefreshing.value = false
                }
            }
        }
    }
    
    fun toggleFavorite() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            val destination = currentState.data
            
            viewModelScope.launch {
                try {
                    destinationRepository.toggleFavorite(destination.id)
                    // Update the UI state with the toggled favorite status
                    _uiState.value = UiState.Success(
                        destination.copy(isFavorite = !destination.isFavorite)
                    )
                } catch (e: Exception) {
                    Timber.e(e, "Error toggling favorite")
                }
            }
        }
    }
}