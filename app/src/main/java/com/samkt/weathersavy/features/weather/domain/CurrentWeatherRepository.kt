package com.samkt.weathersavy.features.weather.domain

import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import com.samkt.weathersavy.utils.Result
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherRepository {
    fun getCurrentWeather(): Flow<Result<CurrentWeather>>
}
