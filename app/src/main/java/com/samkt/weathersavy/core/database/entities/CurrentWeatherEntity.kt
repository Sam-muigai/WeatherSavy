package com.samkt.weathersavy.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val location: String,
    val temperature: String,
    val condition: String,
    val humidity: String,
    val wind: String,
    val feelsLike: String,
)
