package com.samkt.weathersavy.features.weather.domain

import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import com.samkt.weathersavy.utils.Result
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherRepository {
    fun getCurrentWeather(): Flow<CurrentWeather>

    suspend fun syncWeather(
        onSyncSuccess: (CurrentWeather) -> Unit,
        onSyncFailed: (error: Exception) -> Unit,
    )

    fun currentWeatherEmpty(): Flow<Boolean>

    suspend fun saveUserLocation()

    suspend fun getIsOnBoardingDone(): Boolean

    suspend fun getFirstCurrentWeather(): Flow<Result<CurrentWeather>>
}
