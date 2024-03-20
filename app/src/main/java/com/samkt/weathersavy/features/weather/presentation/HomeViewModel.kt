package com.samkt.weathersavy.features.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import com.samkt.weathersavy.worker.SyncingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val currentWeatherRepository: CurrentWeatherRepository,
        private val syncingManager: SyncingManager,
    ) : ViewModel() {
        val currentWeather =
            currentWeatherRepository
                .getCurrentWeather()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = CurrentWeather(),
                )

        val currentWeatherEmpty =
            currentWeatherRepository.currentWeatherEmpty()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = false,
                )
        val isSyncing: StateFlow<Boolean>
            get() =
                syncingManager.isSyncing
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5_000),
                        initialValue = false,
                    )
    }
