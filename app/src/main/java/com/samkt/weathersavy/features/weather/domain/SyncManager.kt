package com.samkt.weathersavy.features.weather.domain

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    val isSyncing: Flow<Boolean>
}
