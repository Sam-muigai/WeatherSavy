package com.samkt.weathersavy.core.network.dtos

import com.squareup.moshi.Json

data class CloudsDto(
    @field:Json(name = "all")
    val all: Int,
)
