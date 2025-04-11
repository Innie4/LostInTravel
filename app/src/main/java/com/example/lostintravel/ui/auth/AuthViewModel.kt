package com.example.lostintravel.ui.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostintravel.domain.model.AuthState
import com.example.lostintravel.domain.repository.AuthRepository
import com.example.lostintravel.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        observeAuthState()
        checkAuthStatus()
    }
    
    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.getAuthState().collectLatest { authState ->
                if (authState.isAuthenticated) {
                    _uiState.value = AuthUiState.Authenticated(authState)
                } else {
                    _uiState.value = AuthUiState.Unauthenticated()
                }
            }
        }
    }
    
    private fun checkAuthStatus() {
        viewModelScope.launch {
            val isAuthenticated = authRepository.isAuthenticated()
            if (!isAuthenticated) {
                _uiState.value = AuthUiState.Unauthenticated()
            }
            // If authenticated, the flow observer will update the state
        }
    }
    
    fun getSignInIntent(): Intent {
        return authRepository.getSignInIntent()
    }
    
    fun signInWithGoogle(data: Intent?) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            
            when (val result = authRepository.signInWithGoogle(data)) {
                is Result.Success -> {
                    _uiState.value = AuthUiState.Authenticated(result.data)
                }
                is Result.Error -> {
                    _uiState.value = AuthUiState.Error(
                        result.exception.message ?: "Authentication failed"
                    )
                }
                is Result.Loading -> {
                    // Already set to loading above
                }
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            
            when (val result = authRepository.signOut()) {
                is Result.Success -> {
                    _uiState.value = AuthUiState.Unauthenticated("Signed out successfully")
                }
                is Result.Error -> {
                    _uiState.value = AuthUiState.Error(
                        result.exception.message ?: "Sign out failed"
                    )
                }
                is Result.Loading -> {
                    // Already set to loading above
                }
            }
        }
    }
    
    fun resetAuthState() {
        if (_uiState.value is AuthUiState.Error) {
            _uiState.value = AuthUiState.Unauthenticated()
        }
    }
    
    fun getIdToken(callback: (String?) -> Unit) {
        viewModelScope.launch {
            when (val result = authRepository.getIdToken()) {
                is Result.Success -> callback(result.data)
                else -> callback(null)
            }
        }
    }
}