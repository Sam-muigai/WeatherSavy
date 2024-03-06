package com.samkt.weathersavy.core.network.dtos

import com.squareup.moshi.Json

data class SysDto(
    @field:Json(name = "country")
    val country: String,
    @field:Json(name = "sunrise")
    val sunrise: Int,
    @field:Json(name = "sunset")
    val sunset: Int,
)
