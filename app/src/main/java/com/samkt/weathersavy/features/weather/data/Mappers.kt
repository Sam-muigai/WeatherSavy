package com.samkt.weathersavy.features.weather.data

import com.samkt.weathersavy.core.database.entities.CurrentWeatherEntity
import com.samkt.weathersavy.core.network.dtos.CurrentWeatherResponseDto
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather

fun CurrentWeatherResponseDto.toCurrentWeatherEntity(): CurrentWeatherEntity {
    return CurrentWeatherEntity(
        location = name,
        temperature = mainDto?.temp ?: 0.0,
        condition = weatherDto[0].main,
        humidity = mainDto?.humidity ?: 0,
        wind = windDto?.speed ?: 0.0,
        feelsLike = mainDto?.humidity ?: 0,
        weatherIcon = weatherDto[0].icon,
    )
}

fun CurrentWeatherEntity.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        location,
        temperature,
        condition,
        humidity,
        wind,
        feelsLike,
        weatherIcon,
    )
}
