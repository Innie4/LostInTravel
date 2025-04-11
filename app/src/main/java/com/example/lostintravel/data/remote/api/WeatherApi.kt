package com.example.lostintravel.data.remote.api

import com.example.lostintravel.data.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherForLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): WeatherDto?
}