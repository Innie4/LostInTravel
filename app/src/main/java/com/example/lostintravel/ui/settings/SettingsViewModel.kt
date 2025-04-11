package com.example.lostintravel.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostintravel.data.preferences.UserPreferencesRepository
import com.example.lostintravel.domain.repository.DestinationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()
    
    private val _isClearingCache = MutableStateFlow(false)
    val isClearingCache: StateFlow<Boolean> = _isClearingCache.asStateFlow()
    
    private val _isRefreshingCache = MutableStateFlow(false)
    val isRefreshingCache: StateFlow<Boolean> = _isRefreshingCache.asStateFlow()
    
    init {
        viewModelScope.launch {
            userPreferencesRepository.getThemeMode().collect { mode ->
                _themeMode.value = mode
            }
        }
    }
    
    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            userPreferencesRepository.setThemeMode(mode)
        }
    }
    
    fun clearCache() {
        viewModelScope.launch {
            try {
                _isClearingCache.value = true
                destinationRepository.clearCache()
            } catch (e: Exception) {
                Timber.e(e, "Error clearing cache")
            } finally {
                _isClearingCache.value = false
            }
        }
    }
    
    fun refreshCache() {
        viewModelScope.launch {
            try {
                _isRefreshingCache.value = true
                destinationRepository.refreshDestinations()
            } catch (e: Exception) {
                Timber.e(e, "Error refreshing cache")
            } finally {
                _isRefreshingCache.value = false
            }
        }
    }
}