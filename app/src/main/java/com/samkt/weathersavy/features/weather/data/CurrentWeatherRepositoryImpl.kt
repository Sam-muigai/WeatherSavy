package com.samkt.weathersavy.features.weather.data

import androidx.room.withTransaction
import com.samkt.weathersavy.core.database.CurrentWeatherDatabase
import com.samkt.weathersavy.core.location.LocationService
import com.samkt.weathersavy.core.network.OpenWeatherApiService
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import com.samkt.weathersavy.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class CurrentWeatherRepositoryImpl
    @Inject
    constructor(
        private val openWeatherApiService: OpenWeatherApiService,
        private val locationService: LocationService,
        private val currentWeatherDatabase: CurrentWeatherDatabase,
    ) : CurrentWeatherRepository {
        override fun getCurrentWeather(): Flow<Result<CurrentWeather>> =
            flow {
                val userLocation = locationService.getLocation()
                if (userLocation != null) {
                    processRequest(
                        userLocation.longitude,
                        userLocation.latitude,
                    )
                } else {
                    // Default to Nairobi if location is not found
                    processRequest(
                        "36.82",
                        "-1.29",
                    )
                }
            }

        private suspend fun FlowCollector<Result<CurrentWeather>>.processRequest(
            longitude: String,
            latitude: String,
        ) {
            val currentWeatherDao = currentWeatherDatabase.dao()
            try {
                val remoteCurrentWeather =
                    openWeatherApiService.getCurrentWeather(
                        longitude,
                        latitude,
                    )
                val localCurrentWeather = remoteCurrentWeather.toCurrentWeatherEntity()
                currentWeatherDatabase.withTransaction {
                    currentWeatherDao.deleteCurrentWeather()
                    currentWeatherDao.insertCurrentWeather(localCurrentWeather)
                }
                val currentWeather =
                    currentWeatherDao.getCurrentWeather().first()[0].toCurrentWeather()
                emit(
                    Result.Success(
                        data = currentWeather,
                    ),
                )
            } catch (exc: Exception) {
                if (currentWeatherDao.getCurrentWeather().first().isEmpty()) {
                    emit(
                        Result.Error(
                            exc.localizedMessage,
                        ),
                    )
                } else {
                    val currentWeather =
                        currentWeatherDao.getCurrentWeather().first()[0].toCurrentWeather()

                    when (exc) {
                        is IOException -> {
                            emit(
                                Result.Success(
                                    data = currentWeather,
                                    message = "No internet connection!!",
                                ),
                            )
                        }

                        else -> {
                            emit(
                                Result.Success(
                                    data = currentWeather,
                                    message = exc.localizedMessage,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
