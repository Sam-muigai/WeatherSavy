package com.samkt.weathersavy.features.weather.data

import android.util.Log
import androidx.room.withTransaction
import com.samkt.weathersavy.core.database.CurrentWeatherDatabase
import com.samkt.weathersavy.core.datastore.CurrentWeatherDataStore
import com.samkt.weathersavy.core.location.LocationService
import com.samkt.weathersavy.core.network.OpenWeatherApiService
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import com.samkt.weathersavy.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "CurrentWeatherRepositoryImpl"

class CurrentWeatherRepositoryImpl
    @Inject
    constructor(
        private val openWeatherApiService: OpenWeatherApiService,
        private val locationService: LocationService,
        private val currentWeatherDatabase: CurrentWeatherDatabase,
        private val currentWeatherDataStore: CurrentWeatherDataStore,
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
            val longitude = currentWeatherDataStore.getUserLongitude().first()
            val latitude = currentWeatherDataStore.getUserLatitude().first()
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

        override fun currentWeatherEmpty(): Flow<Boolean> {
            return currentWeatherDatabase
                .dao()
                .getCurrentWeather()
                .map {
                    it.isEmpty()
                }
        }

        override suspend fun saveUserLocation() {
            val userLocation = locationService.getLocation()
            val longitude = userLocation?.longitude
            val latitude = userLocation?.latitude
            if (longitude != null && latitude != null) {
                currentWeatherDataStore.saveLatitude(latitude)
                currentWeatherDataStore.saveLongitude(longitude)
            }
        }

        override suspend fun getIsOnBoardingDone(): Boolean {
            return currentWeatherDataStore.getIsOnBoardingDone().first()
        }

        override suspend fun getFirstCurrentWeather(): Flow<Result<CurrentWeather>> =
            flow {
                val userLocation = locationService.getLocation()

                val longitude = userLocation?.longitude ?: "32.4"
                val latitude = userLocation?.latitude ?: "-0.67"

                Log.d(TAG, "Longitude: $longitude")
                Log.d(TAG, "Latitude: $latitude")

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
                    val currentWeather: Flow<CurrentWeather> =
                        currentWeatherDao.getCurrentWeather().map { currentWeathers ->
                            currentWeathers.firstOrNull()
                        }
                            .filterNotNull()
                            .map {
                                it.toCurrentWeather()
                            }
                    saveUserLocation()
                    currentWeatherDataStore.saveIsOnBoardingDone(true)
                    emit(Result.Success(currentWeather.first()))
                } catch (exc: Exception) {
                    emit(Result.Error(exc.localizedMessage))
                }
            }
    }
