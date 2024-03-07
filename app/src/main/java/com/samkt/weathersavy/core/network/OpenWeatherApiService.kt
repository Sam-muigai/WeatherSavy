package com.samkt.weathersavy.core.network

import com.samkt.weathersavy.core.network.dtos.CurrentWeatherResponseDto
import com.samkt.weathersavy.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lon") longitude: String,
        @Query("lat") latitude: String,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = "metric",
    ): CurrentWeatherResponseDto
}
