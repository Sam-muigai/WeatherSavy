package com.samkt.weathersavy.features.weather.domain

import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherRepository {
    fun getCurrentWeather(): Flow<CurrentWeather>

    suspend fun syncWeather(
        onSyncSuccess: (CurrentWeather) -> Unit,
        onSyncFailed: (error: Exception) -> Unit,
    )

    fun currentWeatherEmpty(): Flow<Boolean>

    suspend fun saveUserLocation()
}
