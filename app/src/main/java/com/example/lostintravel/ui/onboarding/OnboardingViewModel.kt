package com.example.lostintravel.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostintravel.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    
    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn
    
    init {
        checkUserLoggedIn()
    }
    
    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            val user = getUserUseCase()
            _isUserLoggedIn.value = user != null
        }
    }
}