package com.example.lostintravel.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.repository.AuthRepository
import com.example.lostintravel.domain.repository.DestinationRepository
import com.example.lostintravel.domain.util.Result
import com.example.lostintravel.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val destinationRepository: DestinationRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _destinationsState = MutableStateFlow<UiState<List<Destination>>>(UiState.Loading)
    val destinationsState: StateFlow<UiState<List<Destination>>> = _destinationsState.asStateFlow()

    private val _popularDestinations = MutableStateFlow<UiState<List<Destination>>>(UiState.Loading)
    val popularDestinations: StateFlow<UiState<List<Destination>>> = _popularDestinations.asStateFlow()

    private val _recommendedDestinations = MutableStateFlow<UiState<List<Destination>>>(UiState.Loading)
    val recommendedDestinations: StateFlow<UiState<List<Destination>>> = _recommendedDestinations.asStateFlow()

    private val _favoriteDestinations = MutableStateFlow<UiState<List<Destination>>>(UiState.Loading)
    val favoriteDestinations: StateFlow<UiState<List<Destination>>> = _favoriteDestinations.asStateFlow()

    private val _searchResults = MutableStateFlow<UiState<List<Destination>>>(UiState.Empty)
    val searchResults: StateFlow<UiState<List<Destination>>> = _searchResults.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadDestinations()
        observeFavorites()
    }

    fun loadDestinations() {
        viewModelScope.launch {
            _destinationsState.value = UiState.Loading
            
            when (val result = destinationRepository.getDestinations()) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _destinationsState.value = UiState.Empty
                    } else {
                        _destinationsState.value = UiState.Success(result.data)
                    }
                    loadPopularDestinations()
                    loadRecommendedDestinations()
                }
                is Result.Error -> {
                    _destinationsState.value = UiState.Error(
                        result.exception.message ?: "Failed to load destinations",
                        result.exception
                    )
                }
                is Result.Loading -> {
                    // Already set to loading above
                }
            }
        }
    }

    fun refreshDestinations() {
        viewModelScope.launch {
            _isRefreshing.value = true
            
            when (val result = destinationRepository.refreshDestinations()) {
                is Result.Success -> {
                    loadDestinations()
                }
                is Result.Error -> {
                    _destinationsState.value = UiState.Error(
                        result.exception.message ?: "Failed to refresh destinations",
                        result.exception
                    )
                }
                is Result.Loading -> {
                    // Do nothing
                }
            }
            
            _isRefreshing.value = false
        }
    }

    fun searchDestinations(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _searchResults.value = UiState.Empty
                return@launch
            }
            
            _searchResults.value = UiState.Loading
            
            when (val result = destinationRepository.searchDestinations(query)) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _searchResults.value = UiState.Empty
                    } else {
                        _searchResults.value = UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _searchResults.value = UiState.Error(
                        result.exception.message ?: "Failed to search destinations",
                        result.exception
                    )
                }
                is Result.Loading -> {
                    // Already set to loading above
                }
            }
        }
    }

    fun toggleFavorite(destinationId: String) {
        viewModelScope.launch {
            destinationRepository.toggleFavorite(destinationId)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }

    private fun loadPopularDestinations() {
        viewModelScope.launch {
            _popularDestinations.value = UiState.Loading
            
            when (val result = destinationRepository.getPopularDestinations()) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _popularDestinations.value = UiState.Empty
                    } else {
                        _popularDestinations.value = UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _popularDestinations.value = UiState.Error(
                        result.exception.message ?: "Failed to load popular destinations",
                        result.exception
                    )
                }
                is Result.Loading -> {
                    // Already set to loading above
                }
            }
        }
    }

    private fun loadRecommendedDestinations() {
        viewModelScope.launch {
            _recommendedDestinations.value = UiState.Loading
            
            when (val result = destinationRepository.getRecommendedDestinations()) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _recommendedDestinations.value = UiState.Empty
                    } else {
                        _recommendedDestinations.value = UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _recommendedDestinations.value = UiState.Error(
                        result.exception.message ?: "Failed to load recommended destinations",
                        result.exception
                    )
                }
                is Result.Loading -> {
                    // Already set to loading above
                }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            destinationRepository.getFavoriteDestinations().collect { favorites ->
                _favoriteDestinations.value = if (favorites.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(favorites)
                }
            }
        }
    }
}