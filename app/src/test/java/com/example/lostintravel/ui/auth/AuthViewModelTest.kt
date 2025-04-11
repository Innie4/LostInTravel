package com.example.lostintravel.ui.auth

import android.content.Intent
import com.example.lostintravel.domain.model.AuthState
import com.example.lostintravel.domain.repository.AuthRepository
import com.example.lostintravel.domain.util.DomainException
import com.example.lostintravel.domain.util.Result
import com.example.lostintravel.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AuthViewModelTest {
    
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: AuthViewModel
    
    private val testAuthState = AuthState(
        isAuthenticated = true,
        userId = "test-user-id",
        displayName = "Test User",
        email = "test@example.com",
        photoUrl = "https://example.com/photo.jpg"
    )
    
    @Before
    fun setup() {
        authRepository = mock()
    }
    
    @Test
    fun `init sets Authenticated state when user is authenticated`() = runTest {
        // Arrange
        whenever(authRepository.getAuthState()).thenReturn(flowOf(testAuthState))
        whenever(authRepository.isAuthenticated()).thenReturn(true)
        
        // Act
        viewModel = AuthViewModel(authRepository)
        
        // Assert
        assertTrue(viewModel.uiState.value is AuthUiState.Authenticated)
        assertEquals(testAuthState, (viewModel.uiState.value as AuthUiState.Authenticated).authState)
    }
    
    @Test
    fun `init sets Unauthenticated state when user is not authenticated`() = runTest {
        // Arrange
        whenever(authRepository.getAuthState()).thenReturn(flowOf(AuthState()))
        whenever(authRepository.isAuthenticated()).thenReturn(false)
        
        // Act
        viewModel = AuthViewModel(authRepository)
        
        // Assert
        assertTrue(viewModel.uiState.value is AuthUiState.Unauthenticated)
    }
    
    @Test
    fun `signInWithGoogle sets Authenticated state when sign-in succeeds`() = runTest {
        // Arrange
        whenever(authRepository.getAuthState()).thenReturn(flowOf(AuthState()))
        whenever(authRepository.isAuthenticated()).thenReturn(false)
        whenever(authRepository.signInWithGoogle(any())).thenReturn(Result.success(testAuthState))
        
        viewModel = AuthViewModel(authRepository)
        
        // Act
        viewModel.signInWithGoogle(mock())
        
        // Assert
        assertTrue(viewModel.uiState.value is AuthUiState.Authenticated)
        assertEquals(testAuthState, (viewModel.uiState.value as AuthUiState.Authenticated).authState)
    }
    
    @Test
    fun `signInWithGoogle sets Error state when sign-in fails`() = runTest {
        // Arrange
        val errorMessage = "Authentication failed"
        whenever(authRepository.getAuthState()).thenReturn(flowOf(AuthState()))
        whenever(authRepository.isAuthenticated()).thenReturn(false)
        whenever(authRepository.signInWithGoogle(any())).thenReturn(
            Result.error(DomainException.AuthError(errorMessage))
        )
        
        viewModel = AuthViewModel(authRepository)
        
        // Act
        viewModel.signInWithGoogle(mock())
        
        // Assert
        assertTrue(viewModel.uiState.value is AuthUiState.Error)
        assertEquals(errorMessage, (viewModel.uiState.value as AuthUiState.Error).message)
    }
    
    @Test
    fun `signOut sets Unauthenticated state when sign-out succeeds`() = runTest {
        // Arrange
        whenever(authRepository.getAuthState()).thenReturn(flowOf(testAuthState))
        whenever(authRepository.isAuthenticated()).thenReturn(true)
        whenever(authRepository.signOut()).thenReturn(Result.success(true))
        
        viewModel = AuthViewModel(authRepository)
        
        // Act
        viewModel.signOut()
        
        // Assert
        assertTrue(viewModel.uiState.value is AuthUiState.Unauthenticated)
    }
    
    @Test
    fun `signOut sets Error state when sign-out fails`() = runTest {
        // Arrange
        val errorMessage = "Sign out failed"
        whenever(authRepository.getAuthState()).thenReturn(flowOf(testAuthState))
        whenever(authRepository.isAuthenticated()).thenReturn(true)
        whenever(authRepository.signOut()).thenReturn(
            Result.error(DomainException.AuthError(errorMessage))
        )
        
        viewModel = AuthViewModel(authRepository)
        
        // Act
        viewModel.signOut()
        
        // Assert
        assertTrue(viewModel.uiState.value is AuthUiState.Error)
        assertEquals(errorMessage, (viewModel.uiState.value as AuthUiState.Error).message)
    }
    
    @Test
    fun `getSignInIntent calls repository getSignInIntent method`() = runTest {
        // Arrange
        val mockIntent = mock<Intent>()
        whenever(authRepository.getAuthState()).thenReturn(flowOf(AuthState()))
        whenever(authRepository.isAuthenticated()).thenReturn(false)
        whenever(authRepository.getSignInIntent()).thenReturn(mockIntent)
        
        viewModel = AuthViewModel(authRepository)
        
        // Act
        val result = viewModel.getSignInIntent()
        
        // Assert
        assertEquals(mockIntent, result)
        verify(authRepository).getSignInIntent()
    }
    
    @Test
    fun `resetAuthState resets Error state to Unauthenticated`() = runTest {
        // Arrange
        whenever(authRepository.getAuthState()).thenReturn(flowOf(AuthState()))
        whenever(authRepository.isAuthenticated()).thenReturn(false)
        whenever(authRepository.signInWithGoogle(any())).thenReturn(
            Result.error(DomainException.AuthError("Error"))
        )
        
        viewModel = AuthViewModel(authRepository)
        viewModel.signInWithGoogle(mock())
        
        // Act
        viewModel.resetAuthState()
        
        // Assert
        assertTrue(viewModel.uiState.value is AuthUiState.Unauthenticated)
    }
}