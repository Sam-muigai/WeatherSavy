package com.samkt.weathersavy.features.weather.domain

import com.samkt.weathersavy.features.weather.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserInfo {
    val userData: Flow<UserData>

    suspend fun setUserIsDone(isDone: Boolean)
}
