package com.samkt.weathersavy.core.network.dtos

import com.squareup.moshi.Json

data class CoordDto(
    @field:Json(name = "lat")
    val lat: Double,
    @field:Json(name = "lon")
    val lon: Double,
)
