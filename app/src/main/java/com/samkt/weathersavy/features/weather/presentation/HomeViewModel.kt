package com.samkt.weathersavy.features.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import com.samkt.weathersavy.utils.Result
import com.samkt.weathersavy.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val currentWeatherRepository: CurrentWeatherRepository,
    ) : ViewModel() {
        private val _homeScreenUiState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
        val homeScreenUiState = _homeScreenUiState.asStateFlow()

        private val _uiEvents = Channel<UiEvents>()
        val uiEvents = _uiEvents.receiveAsFlow()

        private val _isOnBoardingDone = MutableStateFlow<Boolean>(false)
        val isOnBoardingDone = _isOnBoardingDone.asStateFlow()

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
                _isOnBoardingDone.update {
                    currentWeatherRepository.getIsOnBoardingDone()
                }
                if (currentWeatherRepository.getIsOnBoardingDone()) {
                    currentWeatherRepository.getCurrentWeather().collectLatest { weather ->
                        _homeScreenUiState.value =
                            HomeScreenState.Success(
                                weather,
                            )
                    }
                } else {
                    refreshCurrentWeather()
                }
            }
        }

        fun refreshCurrentWeather() {
            viewModelScope.launch {
                _homeScreenUiState.value =
                    HomeScreenState.Loading
                currentWeatherRepository.getFirstCurrentWeather().collectLatest { result ->
                    when (result) {
                        is Result.Error -> {
                            _homeScreenUiState.value =
                                HomeScreenState.Error(
                                    result.message ?: "Unknown error occurred!!",
                                )
                        }

                        is Result.Success -> {
                            _homeScreenUiState.value =
                                HomeScreenState.Success(
                                    result.data ?: CurrentWeather(),
                                )
                            _isOnBoardingDone.update {
                                currentWeatherRepository.getIsOnBoardingDone()
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
    val errorOccurred: Boolean = false,
)

sealed interface HomeScreenState {
    data class Success(
        val currentWeather: CurrentWeather,
    ) : HomeScreenState

    data class Error(val message: String) : HomeScreenState

    data object Loading : HomeScreenState
}
