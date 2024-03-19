package com.samkt.weathersavy.features.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val currentWeatherRepository: CurrentWeatherRepository,
    ) : ViewModel() {
        val currentWeather =
            currentWeatherRepository
                .getCurrentWeather()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = CurrentWeather(),
                )

        init {
            getCurrentWeather()
        }

        private fun getCurrentWeather() {
        }
    }
