package com.samkt.weathersavy.features.weather.data

import com.samkt.apiResult.ApiResult
import com.samkt.apiResult.handleApiCall
import com.samkt.weathersavy.core.location.LocationService
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
        private val locationService: LocationService,
    ) : CurrentWeatherRepository {
        override fun getCurrentWeather(): Flow<ApiResult<CurrentWeather>> =
            flow {
                val userLocation = locationService.getLocation()
                if (userLocation != null) {
                    emit(
                        handleApiCall {
                            openWeatherApiService.getCurrentWeather(
                                userLocation.longitude,
                                userLocation.latitude,
                            ).toCurrentWeather()
                        },
                    )
                } else {
                    // Default to Nairobi if location is not found
                    emit(
                        handleApiCall {
                            openWeatherApiService.getCurrentWeather("36.82", "-1.29")
                                .toCurrentWeather()
                        },
                    )
                }
            }
    }
