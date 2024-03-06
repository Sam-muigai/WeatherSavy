package com.samkt.weathersavy.features.weather.data

import com.samkt.apiResult.ApiResult
import com.samkt.apiResult.handleApiCall
import com.samkt.weathersavy.core.network.OpenWeatherApiService
import com.samkt.weathersavy.features.weather.data.model.CurrentWeather
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrentWeatherRepositoryImpl
    @Inject
    constructor(
        private val openWeatherApiService: OpenWeatherApiService,
    ) : CurrentWeatherRepository {
        override fun getCurrentWeather(
            longitude: String,
            latitude: String,
        ): Flow<ApiResult<CurrentWeather>> =
            flow {
                emit(
                    handleApiCall {
                        openWeatherApiService.getCurrentWeather(longitude, latitude).toCurrentWeather()
                    },
                )
            }
    }
