package com.samkt.weathersavy.features.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import com.samkt.weathersavy.utils.Result
import com.samkt.weathersavy.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

        private val _uiEvents = MutableSharedFlow<UiEvents>()
        val uiEvents = _uiEvents.asSharedFlow()

        init {
            getCurrentWeather()
        }

        private fun getCurrentWeather() {
            _homeScreenState.value = HomeScreenState.Loading

            currentWeatherRepository.getCurrentWeather()
                .onEach { result ->
                    when (result) {
                        is Result.Error -> {
                            _homeScreenState.value =
                                HomeScreenState.Error(result.message ?: "Unknown error occurred")
                        }

                        is Result.Success -> {
                            result.data?.let {
                                _homeScreenState.value = HomeScreenState.Success(result.data)
                            }
                            result.message?.let {
                                _uiEvents.emit(UiEvents.ShowSnackBar(result.message))
                            }
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
