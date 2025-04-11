package com.example.lostintravel.ui.home

import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.repository.AuthRepository
import com.example.lostintravel.domain.repository.DestinationRepository
import com.example.lostintravel.domain.util.DomainException
import com.example.lostintravel.domain.util.Result
import com.example.lostintravel.util.MainCoroutineRule
import com.example.lostintravel.util.MockResponseGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    
    private lateinit var destinationRepository: DestinationRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: HomeViewModel
    
    private val testDestinations = listOf(
        MockResponseGenerator.createMockDestination(id = "1"),
        MockResponseGenerator.createMockDestination(id = "2"),
        MockResponseGenerator.createMockDestination(id = "3")
    )
    
    @Before
    fun setup() {
        destinationRepository = mock()
        authRepository = mock()
        
        // Default behavior
        whenever(destinationRepository.getFavoriteDestinations()).thenReturn(flowOf(emptyList()))
    }
    
    @Test
    fun `loadDestinations sets Success state when repository returns data`() = runTest {
        // Arrange
        whenever(destinationRepository.getDestinations()).thenReturn(Result.success(testDestinations))
        whenever(destinationRepository.getPopularDestinations()).thenReturn(Result.success(testDestinations))
        whenever(destinationRepository.getRecommendedDestinations()).thenReturn(Result.success(testDestinations))
        
        // Act
        viewModel = HomeViewModel(destinationRepository, authRepository)
        
        // Assert
        assertTrue(viewModel.uiState.value is HomeUiState.Success)
        assertEquals(testDestinations, (viewModel.uiState.value as HomeUiState.Success).destinations)
    }
    
    @Test
    fun `loadDestinations sets Empty state when repository returns empty list`() = runTest {
        // Arrange
        whenever(destinationRepository.getDestinations()).thenReturn(Result.success(emptyList()))
        
        // Act
        viewModel = HomeViewModel(destinationRepository, authRepository)
        
        // Assert
        assertTrue(viewModel.uiState.value is HomeUiState.Empty)
    }
    
    @Test
    fun `loadDestinations sets Error state when repository returns error`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        whenever(destinationRepository.getDestinations()).thenReturn(
            Result.error(DomainException.NetworkError(errorMessage))
        )
        
        // Act
        viewModel = HomeViewModel(destinationRepository, authRepository)
        
        // Assert
        assertTrue(viewModel.uiState.value is HomeUiState.Error)
        assertEquals(errorMessage, (viewModel.uiState.value as HomeUiState.Error).message)
    }
    
    @Test
    fun `refreshDestinations calls repository refresh method`() = runTest {
        // Arrange
        whenever(destinationRepository.getDestinations()).thenReturn(Result.success(testDestinations))
        whenever(destinationRepository.refreshDestinations()).thenReturn(Result.success(true))
        whenever(destinationRepository.getPopularDestinations()).thenReturn(Result.success(testDestinations))
        whenever(destinationRepository.getRecommendedDestinations()).thenReturn(Result.success(testDestinations))
        
        viewModel = HomeViewModel(destinationRepository, authRepository)
        
        // Act
        viewModel.refreshDestinations()
        
        // Assert
        verify(destinationRepository).refreshDestinations()
    }
    
    @Test
    fun `toggleFavorite calls repository toggleFavorite method`() = runTest {
        // Arrange
        whenever(destinationRepository.getDestinations()).thenReturn(Result.success(testDestinations))
        whenever(destinationRepository.getPopularDestinations()).thenReturn(Result.success(testDestinations))
        whenever(destinationRepository.getRecommendedDestinations()).thenReturn(Result.success(testDestinations))
        
        viewModel = HomeViewModel(destinationRepository, authRepository)
        
        // Act
        viewModel.toggleFavorite("1")
        
        // Assert
        verify(destinationRepository).toggleFavorite("1")
    }
    
    @Test
    fun `signOut calls authRepository signOut method`() = runTest {
        // Arrange
        whenever(destinationRepository.getDestinations()).thenReturn(Result.success(testDestinations))
        whenever(destinationRepository.getPopularDestinations()).thenReturn(Result.success(testDestinations))
        whenever(destinationRepository.getRecommendedDestinations()).thenReturn(Result.success(testDestinations))
        
        viewModel = HomeViewModel(destinationRepository, authRepository)
        
        // Act
        viewModel.signOut()
        
        // Assert
        verify(authRepository).signOut()
    }
}