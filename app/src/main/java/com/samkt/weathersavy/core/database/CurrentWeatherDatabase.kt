package com.samkt.weathersavy.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samkt.weathersavy.core.database.entities.CurrentWeatherEntity

@Database(
    entities = [CurrentWeatherEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class CurrentWeatherDatabase : RoomDatabase() {
    abstract fun dao(): CurrentWeatherDao
}
