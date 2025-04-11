package com.example.lostintravel.data.repository

import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.domain.model.WeatherCondition
import com.example.lostintravel.domain.model.WeatherForecast
import com.example.lostintravel.domain.model.WeatherInfo
import com.example.lostintravel.domain.repository.DestinationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

/**
 * Mock implementation of DestinationRepository for testing and development
 */
class MockDestinationRepositoryImpl @Inject constructor() : DestinationRepository {

    private val frequentlyVisitedDestinations = listOf(
        Destination(
            id = "1",
            name = "Mykonos",
            country = "Greece",
            city = "Chora",
            price = 1800.0,
            imageUrl = "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff",
            description = "Mykonos is a Greek island, part of the Cyclades, lying between Tinos, Syros, Paros and Naxos. The island is famous for its vibrant nightlife, pristine beaches, and traditional Cycladic architecture.",
            isFrequentlyVisited = true,
            visitCount = 15000
        ),
        Destination(
            id = "2",
            name = "Waterfort",
            country = "Italy",
            city = "Venesia",
            price = 1800.0,
            imageUrl = "https://images.unsplash.com/photo-1498503182468-3b51cbb6cb24",
            description = "Waterfort is a beautiful coastal town in Italy known for its crystal clear waters and historic fortifications. Visitors can enjoy the Mediterranean climate, delicious cuisine, and rich cultural heritage.",
            isFrequentlyVisited = true,
            visitCount = 12000
        ),
        Destination(
            id = "3",
            name = "Delli",
            country = "Indonesia",
            city = "Bali",
            price = 1200.0,
            imageUrl = "https://images.unsplash.com/photo-1537996194471-e657df975ab4",
            description = "Delli is a hidden gem in Bali, Indonesia, offering stunning landscapes, lush rice terraces, and spiritual temples. It's the perfect destination for travelers seeking tranquility and natural beauty.",
            isFrequentlyVisited = true,
            visitCount = 9000
        )
    )

    private val recommendedDestinations = listOf(
        Destination(
            id = "4",
            name = "Kigali Resort",
            country = "Rwanda",
            city = "Kigali",
            price = 1350.0,
            imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4",
            description = "Kigali Resort offers a luxurious retreat in the heart of Rwanda. Surrounded by lush greenery and wildlife, it's the perfect base for exploring the country's natural wonders and vibrant culture.",
            isRecommended = true,
            visitCount = 5000
        ),
        Destination(
            id = "5",
            name = "Maldives",
            country = "Rep of Maldives",
            city = "Maldives",
            price = 1350.0,
            imageUrl = "https://images.unsplash.com/photo-1514282401047-d79a71a590e8",
            description = "The Maldives is a tropical paradise in the Indian Ocean, known for its overwater bungalows, crystal clear turquoise waters, and vibrant coral reefs. It's the perfect destination for honeymooners and luxury travelers seeking privacy and natural beauty.",
            isRecommended = true,
            visitCount = 8000
        ),
        Destination(
            id = "6",
            name = "Sumbing Mount",
            country = "Greece",
            city = "Chora",
            price = 1350.0,
            imageUrl = "https://images.unsplash.com/photo-1506929562872-bb421503ef21",
            description = "Mount Sumbing or Gunung Sumbing is an active, stratovolcano in Central Java, Indonesia that is symmetrical like its neighbour, Mount Sindoro. The mountain offers breathtaking views and challenging hiking trails for adventure enthusiasts.",
            isRecommended = true,
            visitCount = 100000
        )
    )

    override fun getFrequentlyVisitedDestinations(): Flow<List<Destination>> = flow {
        delay(1000) // Simulate network delay
        emit(frequentlyVisitedDestinations)
    }

    override fun getRecommendedDestinations(): Flow<List<Destination>> = flow {
        delay(1000) // Simulate network delay
        emit(recommendedDestinations)
    }

    override suspend fun getDestinationDetails(id: String): Result<Destination> {
        delay(800) // Simulate network delay
        
        val destination = (frequentlyVisitedDestinations + recommendedDestinations).find { it.id == id }
        
        return if (destination != null) {
            Result.success(destination)
        } else {
            Result.failure(Exception("Destination not found"))
        }
    }

    override suspend fun getWeatherForecast(city: String, country: String): Result<WeatherForecast> {
        delay(500) // Simulate network delay
        
        // Create mock weather data
        val currentWeather = WeatherInfo(
            time = "Now",
            temperature = 24.0,
            condition = WeatherCondition.SUNNY
        )
        
        val hourlyForecast = listOf(
            currentWeather,
            WeatherInfo(
                time = "1:00PM",
                temperature = 25.0,
                condition = WeatherCondition.PARTLY_CLOUDY
            ),
            WeatherInfo(
                time = "2:00PM",
                temperature = 23.0,
                condition = WeatherCondition.CLOUDY
            ),
            WeatherInfo(
                time = "3:00PM",
                temperature = 20.0,
                condition = WeatherCondition.PARTLY_CLOUDY
            )
        )
        
        return Result.success(
            WeatherForecast(
                current = currentWeather,
                hourly = hourlyForecast
            )
        )
    }
}