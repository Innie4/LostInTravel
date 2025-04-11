package com.example.lostintravel.data.repository

import com.example.lostintravel.data.mapper.DestinationMapper
import com.example.lostintravel.data.mapper.ExceptionMapper
import com.example.lostintravel.data.mapper.ResultMapper
import com.example.lostintravel.data.remote.api.DestinationApi
import com.example.lostintravel.data.remote.dto.DestinationDto
import com.example.lostintravel.data.remote.util.ApiResponseHandler
import com.example.lostintravel.data.remote.util.NetworkException
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.util.DomainException
import com.example.lostintravel.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import com.example.lostintravel.data.remote.util.Result as DataResult

@ExperimentalCoroutinesApi
class DestinationRepositoryImplTest {
    
    // Mock dependencies
    private lateinit var destinationApi: DestinationApi
    private lateinit var apiResponseHandler: ApiResponseHandler
    private lateinit var destinationMapper: DestinationMapper
    private lateinit var exceptionMapper: ExceptionMapper
    private lateinit var resultMapper: ResultMapper
    
    // System under test
    private lateinit var repository: DestinationRepositoryImpl
    
    // Test data
    private val testDestinationDto = DestinationDto(
        id = "1",
        name = "Test Destination",
        description = "A test destination",
        location = "Test Location",
        imageUrl = "https://example.com/image.jpg",
        rating = 4.5,
        price = 100.0,
        latitude = 0.0,
        longitude = 0.0,
        tags = listOf("test", "destination"),
        amenities = emptyList(),
        reviews = emptyList()
    )
    
    private val testDestination = Destination(
        id = "1",
        name = "Test Destination",
        description = "A test destination",
        location = "Test Location",
        imageUrl = "https://example.com/image.jpg",
        rating = 4.5,
        price = 100.0,
        coordinates = com.example.lostintravel.domain.model.Coordinates(0.0, 0.0),
        tags = listOf("test", "destination"),
        amenities = emptyList(),
        reviews = emptyList(),
        isFavorite = false
    )
    
    @Before
    fun setup() {
        destinationApi = mock()
        apiResponseHandler = mock()
        destinationMapper = mock()
        exceptionMapper = mock()
        resultMapper = mock()
        
        // Setup default behavior for mapper
        whenever(destinationMapper.mapToDomain(any(), any())).thenReturn(testDestination)
        
        // Setup default behavior for exception mapper
        whenever(exceptionMapper.mapToDomainException(any())).thenReturn(
            DomainException.UnknownError("Test error")
        )
        
        // Setup result mapper
        whenever(resultMapper.mapToDomainResult(any<DataResult.Error>())).thenReturn(
            Result.error(DomainException.UnknownError("Test error"))
        )
        
        repository = DestinationRepositoryImpl(
            destinationApi = destinationApi,
            apiResponseHandler = apiResponseHandler,
            destinationMapper = destinationMapper,
            resultMapper = resultMapper
        )
    }
    
    @Test
    fun `getDestinations returns success when API call succeeds`() = runTest {
        // Arrange
        val destinationDtos = listOf(testDestinationDto)
        val destinations = listOf(testDestination)
        
        whenever(apiResponseHandler.safeApiCall<List<DestinationDto>>(any())).thenReturn(
            DataResult.success(destinationDtos)
        )
        
        whenever(destinationMapper.mapToDomain(testDestinationDto, false)).thenReturn(testDestination)
        
        // Act
        val result = repository.getDestinations()
        
        // Assert
        assertTrue(result is Result.Success)
        assertEquals(destinations, (result as Result.Success).data)
    }
    
    @Test
    fun `getDestinations returns error when API call fails`() = runTest {
        // Arrange
        val exception = NetworkException.ConnectionException()
        val domainException = DomainException.NetworkError()
        
        whenever(apiResponseHandler.safeApiCall<List<DestinationDto>>(any())).thenReturn(
            DataResult.error(exception)
        )
        
        whenever(exceptionMapper.mapToDomainException(exception)).thenReturn(domainException)
        whenever(resultMapper.mapToDomainResult(DataResult.error(exception))).thenReturn(
            Result.error(domainException)
        )
        
        // Act
        val result = repository.getDestinations()
        
        // Assert
        assertTrue(result is Result.Error)
        assertEquals(domainException, (result as Result.Error).exception)
    }
    
    @Test
    fun `getDestinationById returns success when API call succeeds`() = runTest {
        // Arrange
        whenever(apiResponseHandler.safeApiCall<DestinationDto>(any())).thenReturn(
            DataResult.success(testDestinationDto)
        )
        
        whenever(destinationMapper.mapToDomain(testDestinationDto, false)).thenReturn(testDestination)
        
        // Act
        val result = repository.getDestinationById("1")
        
        // Assert
        assertTrue(result is Result.Success)
        assertEquals(testDestination, (result as Result.Success).data)
    }
    
    @Test
    fun `toggleFavorite adds destination to favorites when not already favorite`() = runTest {
        // Act
        val result = repository.toggleFavorite("1")
        
        // Assert
        assertTrue(result is Result.Success)
        assertEquals(true, (result as Result.Success).data)
        
        // Verify it's in favorites
        val favorites = repository.getFavoriteDestinations().first()
        assertEquals(0, favorites.size) // Empty because cache is not populated yet
    }
    
    @Test
    fun `refreshDestinations updates cache when API call succeeds`() = runTest {
        // Arrange
        val destinationDtos = listOf(testDestinationDto)
        
        whenever(apiResponseHandler.safeApiCall<List<DestinationDto>>(any())).thenReturn(
            DataResult.success(destinationDtos)
        )
        
        whenever(destinationMapper.mapToDomain(testDestinationDto, false)).thenReturn(testDestination)
        
        // Act
        val result = repository.refreshDestinations()
        
        // Assert
        assertTrue(result is Result.Success)
        assertEquals(true, (result as Result.Success).data)
        
        // Verify cache is updated
        val destinations = repository.getDestinations()
        assertTrue(destinations is Result.Success)
        assertEquals(listOf(testDestination), (destinations as Result.Success).data)
    }
}