package com.samkt.weathersavy.features.weather.domain.model

data class CurrentWeather(
    val location: String = "",
    val temperature: Double = 0.0,
    val condition: String = "",
    val humidity: Int = 0,
    val wind: Double = 0.0,
    val feelsLike: Int = 0,
    val weatherIcon: String = "",
)
