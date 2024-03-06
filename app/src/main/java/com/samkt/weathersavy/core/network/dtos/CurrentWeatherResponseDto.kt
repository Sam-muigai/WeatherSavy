package com.samkt.weathersavy.core.network.dtos

import com.squareup.moshi.Json

data class CurrentWeatherResponseDto(
    @field:Json(name = "base")
    val base: String,
    @field:Json(name = "clouds")
    val cloudsDto: CloudsDto?,
    @field:Json(name = "cod")
    val cod: Int,
    @field:Json(name = "coord")
    val coordDto: CoordDto?,
    @field:Json(name = "dt")
    val dt: Int,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "main")
    val mainDto: MainDto?,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "rain")
    val rainDto: RainDto?,
    @field:Json(name = "sys")
    val sysDto: SysDto?,
    @field:Json(name = "timezone")
    val timezone: Int,
    @field:Json(name = "visibility")
    val visibility: Int,
    @field:Json(name = "weather")
    val weatherDto: List<WeatherDto>,
    @field:Json(name = "wind")
    val windDto: WindDto?,
)
