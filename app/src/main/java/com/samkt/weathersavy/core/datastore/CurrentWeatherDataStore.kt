package com.samkt.weathersavy.core.datastore

import kotlinx.coroutines.flow.Flow

interface CurrentWeatherDataStore {
    fun getUserLongitude(): Flow<String>

    suspend fun saveLongitude(longitude: String)

    fun getUserLatitude(): Flow<String>

    suspend fun saveLatitude(latitude: String)
}
