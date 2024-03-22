package com.samkt.weathersavy.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val location: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val wind: Double,
    val feelsLike: Int,
    val weatherIcon: String,
    val lastUpdated: String,
)
