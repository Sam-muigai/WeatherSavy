package com.samkt.weathersavy.features.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import com.samkt.weathersavy.utils.Result
import com.samkt.weathersavy.worker.SyncingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val currentWeatherRepository: CurrentWeatherRepository,
        private val syncingManager: SyncingManager,
    ) : ViewModel() {
        private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
        val homeScreenUiState = _homeScreenUiState.asStateFlow()

        init {
        /*
         * Update the user location everytime the application launches
         * */
            viewModelScope.launch {
                currentWeatherRepository.saveUserLocation()
            }
            getCurrentWeather()
        }

        private fun getCurrentWeather() {
            viewModelScope.launch {
                _homeScreenUiState.update {
                    it.copy(
                        isOnBoardingDone = currentWeatherRepository.getIsOnBoardingDone(),
                    )
                }
                if (currentWeatherRepository.getIsOnBoardingDone()) {
                    currentWeatherRepository.getCurrentWeather().collectLatest { weather ->
                        _homeScreenUiState.update {
                            it.copy(
                                isLoading = false,
                                currentWeather = weather,
                            )
                        }
                    }
                } else {
                    refreshCurrentWeather()
                }
            }
        }

        fun refreshCurrentWeather() {
            viewModelScope.launch {
                _homeScreenUiState.update {
                    it.copy(
                        isLoading = true,
                    )
                }
                currentWeatherRepository.getFirstCurrentWeather().collectLatest { result ->
                    when (result) {
                        is Result.Error -> {
                            _homeScreenUiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }
                        }

                        is Result.Success -> {
                            _homeScreenUiState.update {
                                it.copy(
                                    isLoading = false,
                                    currentWeather = result.data ?: CurrentWeather(),
                                    isOnBoardingDone = currentWeatherRepository.getIsOnBoardingDone(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val currentWeather: CurrentWeather = CurrentWeather(),
    val isOnBoardingDone: Boolean = false,
)
