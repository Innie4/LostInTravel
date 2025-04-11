package com.example.lostintravel.domain.model

data class WeatherForecast(
    val current: WeatherInfo,
    val hourly: List<WeatherInfo>
)

data class WeatherInfo(
    val time: String,
    val temperature: Double,
    val condition: WeatherCondition
)

enum class WeatherCondition {
    SUNNY, PARTLY_CLOUDY, CLOUDY, RAINY, STORMY
}