package com.samkt.weathersavy.features.weather.domain

import com.samkt.apiResult.ApiResult
import com.samkt.weathersavy.features.weather.data.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherRepository {
    fun getCurrentWeather(): Flow<ApiResult<CurrentWeather>>
}
