package com.samkt.weathersavy.features.weather.data

import androidx.room.withTransaction
import com.samkt.weathersavy.core.database.CurrentWeatherDatabase
import com.samkt.weathersavy.core.location.LocationService
import com.samkt.weathersavy.core.network.OpenWeatherApiService
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrentWeatherRepositoryImpl
    @Inject
    constructor(
        private val openWeatherApiService: OpenWeatherApiService,
        private val locationService: LocationService,
        private val currentWeatherDatabase: CurrentWeatherDatabase,
    ) : CurrentWeatherRepository {
        override fun getCurrentWeather(): Flow<CurrentWeather> {
            val currentWeatherDao = currentWeatherDatabase.dao()
            return currentWeatherDao.getCurrentWeather().map { currentWeathers ->
                currentWeathers.firstOrNull()
            }
                .filterNotNull()
                .map {
                    it.toCurrentWeather()
                }
        }

        override suspend fun syncWeather(
            onSyncSuccess: (CurrentWeather) -> Unit,
            onSyncFailed: (error: Exception) -> Unit,
        ) {
            val currentWeatherDao = currentWeatherDatabase.dao()
            val userLocation = locationService.getLocation()
            val longitude = userLocation?.longitude ?: "32.4"
            val latitude = userLocation?.latitude ?: "-0.67"
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
                val currentWeather: Flow<CurrentWeather> =
                    currentWeatherDao.getCurrentWeather().map { currentWeathers ->
                        currentWeathers.firstOrNull()
                    }
                        .filterNotNull()
                        .map {
                            it.toCurrentWeather()
                        }
                onSyncSuccess.invoke(currentWeather.first())
            } catch (exc: Exception) {
                exc.printStackTrace()
                onSyncFailed.invoke(exc)
            }
        }

        private suspend fun processRequest(
            longitude: String,
            latitude: String,
        ): CurrentWeather {
            val currentWeatherDao = currentWeatherDatabase.dao()
            return try {
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
                val currentWeather: Flow<CurrentWeather> =
                    currentWeatherDao.getCurrentWeather().map { currentWeathers ->
                        currentWeathers.firstOrNull()
                    }
                        .filterNotNull()
                        .map {
                            it.toCurrentWeather()
                        }
                currentWeather.first()
            } catch (exc: Exception) {
                if (currentWeatherDao.getCurrentWeather().first().isEmpty()) {
                    CurrentWeather()
                } else {
                    val currentWeather: Flow<CurrentWeather> =
                        currentWeatherDao.getCurrentWeather().map { currentWeathers ->
                            currentWeathers.firstOrNull()
                        }
                            .filterNotNull()
                            .map {
                                it.toCurrentWeather()
                            }
                    currentWeather.first()
                }
            }
        }
    }
