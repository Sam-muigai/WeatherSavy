package com.samkt.weathersavy.features.weather.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.samkt.weathersavy.features.weather.data.model.CurrentWeather

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    AnimatedContent(
        targetState = homeViewModel.homeScreenState.collectAsState().value,
        label = "",
    ) { homeScreenState: HomeScreenState ->
        when (homeScreenState) {
            is HomeScreenState.Error -> {
                HomeScreenError(
                    errorMessage = homeScreenState.message,
                )
            }

            HomeScreenState.Loading -> {
                HomeLoadingScreen(modifier = modifier)
            }

            is HomeScreenState.Success -> {
                HomeScreenContent(currentWeather = homeScreenState.data)
            }
        }
    }
}

@Composable
fun HomeScreenError(
    modifier: Modifier = Modifier,
    errorMessage: String = "",
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = errorMessage)
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    currentWeather: CurrentWeather,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = currentWeather.name)
    }
}

@Composable
fun HomeLoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
