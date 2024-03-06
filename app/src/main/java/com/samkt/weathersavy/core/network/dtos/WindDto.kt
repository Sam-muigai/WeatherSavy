package com.samkt.weathersavy.core.network.dtos

import com.squareup.moshi.Json

data class WindDto(
    @field:Json(name = "deg")
    val deg: Int,
    @field:Json(name = "gust")
    val gust: Double,
    @field:Json(name = "speed")
    val speed: Double,
)
