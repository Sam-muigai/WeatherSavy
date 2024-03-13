package com.samkt.weathersavy.features.weather.domain.model

data class CurrentWeather(
    val location: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val wind: Double,
    val feelsLike: Int,
    val weatherIcon: String,
)
