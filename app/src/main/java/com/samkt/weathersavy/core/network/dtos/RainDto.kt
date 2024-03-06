package com.samkt.weathersavy.core.network.dtos

import com.squareup.moshi.Json

data class RainDto(
    @field:Json(name = "1h")
    val h: Double,
)
