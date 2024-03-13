package com.samkt.weathersavy.features.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samkt.apiResult.ApiResult
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val currentWeatherRepository: CurrentWeatherRepository,
    ) : ViewModel() {
        private val _homeScreenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
        val homeScreenState = _homeScreenState.asStateFlow()

        init {
            getCurrentWeather()
        }

        private fun getCurrentWeather() {
            _homeScreenState.value = HomeScreenState.Loading
            currentWeatherRepository.getCurrentWeather()
                .onEach { apiResult ->
                    when (apiResult) {
                        is ApiResult.Error -> {
                            _homeScreenState.value =
                                HomeScreenState.Error(apiResult.message ?: "Unknown error occurred")
                        }

                        is ApiResult.Success -> {
                            _homeScreenState.value = HomeScreenState.Success(apiResult.data)
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

sealed interface HomeScreenState {
    data class Error(val message: String) : HomeScreenState

    data class Success(val data: CurrentWeather) : HomeScreenState

    data object Loading : HomeScreenState
}
