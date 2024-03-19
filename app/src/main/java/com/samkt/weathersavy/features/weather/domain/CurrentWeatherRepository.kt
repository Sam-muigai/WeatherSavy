package com.samkt.weathersavy.features.weather.domain

import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherRepository {
    fun getCurrentWeather(): Flow<CurrentWeather>
}
