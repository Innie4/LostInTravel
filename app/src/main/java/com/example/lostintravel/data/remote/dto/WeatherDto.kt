package com.example.lostintravel.data.remote.dto

import com.example.lostintravel.domain.model.Weather

data class WeatherDto(
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double
) {
    fun toDomain(): Weather {
        return Weather(
            temperature = temperature,
            condition = condition,
            humidity = humidity,
            windSpeed = windSpeed
        )
    }
}