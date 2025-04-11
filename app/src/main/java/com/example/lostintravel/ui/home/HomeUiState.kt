package com.example.lostintravel.ui.home

import com.example.lostintravel.domain.model.Destination

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val destinations: List<Destination>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    object Empty : HomeUiState()
}