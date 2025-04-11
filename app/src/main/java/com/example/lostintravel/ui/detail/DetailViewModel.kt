package com.example.lostintravel.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.model.WeatherForecast
import com.example.lostintravel.domain.repository.DestinationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    fun loadDestinationDetails(destinationId: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            
            try {
                val destinationResult = destinationRepository.getDestinationDetails(destinationId)
                
                destinationResult.onSuccess { destination ->
                    val weatherResult = destinationRepository.getWeatherForecast(
                        city = destination.city,
                        country = destination.country
                    )
                    
                    _uiState.value = DetailUiState.Success(
                        destination = destination,
                        weather = weatherResult.getOrNull()
                    )
                }.onFailure { error ->
                    _uiState.value = DetailUiState.Error(error.message ?: "Failed to load destination details")
                }
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(
        val destination: Destination,
        val weather: WeatherForecast?
    ) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}