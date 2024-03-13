package com.samkt.weathersavy.core.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samkt.weathersavy.core.database.entities.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeatherEntity: CurrentWeatherEntity)

    @Query("SELECT * FROM currentweatherentity")
    fun getCurrentWeather(): Flow<List<CurrentWeatherEntity>>

    @Query("DELETE FROM currentweatherentity")
    suspend fun deleteCurrentWeather()
}
