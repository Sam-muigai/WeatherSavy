package com.samkt.weathersavy.features.weather.presentation

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.samkt.weathersavy.R
import com.samkt.weathersavy.features.weather.domain.model.CurrentWeather
import com.samkt.weathersavy.utils.getBackgroundImage
import com.samkt.weathersavy.utils.getTodayDate
import com.samkt.weathersavy.utils.getWeatherIcon
import com.samkt.weathersavy.worker.SyncingWorker

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    context: Context,
) {
    val snackBarState = remember { SnackbarHostState() }
    val isOnBoardingDone by homeViewModel.isOnBoardingDone.collectAsState()
    LaunchedEffect(
        key1 = isOnBoardingDone,
        block = {
            if (isOnBoardingDone) {
                SyncingWorker.startSyncing(context)
            }
        },
    )
    val homeScreenState = homeViewModel.homeScreenUiState.collectAsState().value

    HomeScreenContent(
        snackBarState = snackBarState,
        homeScreenState = homeScreenState,
        date = getTodayDate(),
        modifier = modifier,
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    date: String = "March 06",
    snackBarState: SnackbarHostState,
    homeScreenState: HomeScreenState,
) {
    val context = LocalContext.current
    var imageUrl by remember {
        mutableStateOf("")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackBarState)
        },
    ) { paddingValues ->
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model =
                    ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(500)
                        .build(),
                contentDescription = "background",
                placeholder = painterResource(id = R.drawable.clear_sky),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.Gray, blendMode = BlendMode.Modulate),
                error = painterResource(id = R.drawable.clear_sky),
                fallback = painterResource(id = R.drawable.clear_sky),
            )
            AnimatedContent(
                targetState = homeScreenState,
                transitionSpec = {
                    fadeIn() togetherWith
                        fadeOut()
                },
                label = "",
            ) { state ->
                when (state) {
                    is HomeScreenState.Error -> Unit
                    HomeScreenState.Loading -> HomeScreenLoading()
                    is HomeScreenState.Success -> {
                        val currentWeather = state.currentWeather
                        imageUrl = currentWeather.condition.getBackgroundImage()
                        WeatherDetails(
                            modifier.padding(paddingValues),
                            currentWeather = currentWeather,
                            date = date,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDetails(
    modifier: Modifier = Modifier,
    currentWeather: CurrentWeather,
    date: String,
) {
    val context = LocalContext.current
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = currentWeather.location,
                style =
                    MaterialTheme.typography.bodyLarge,
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = date,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Normal,
                ),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Updated ${currentWeather.lastUpdated}",
            style =
                MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(32.dp))
        AsyncImage(
            modifier = Modifier.size(48.dp),
            model =
                ImageRequest.Builder(context)
                    .data(currentWeather.weatherIcon.getWeatherIcon())
                    .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = currentWeather.condition,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text =
                buildAnnotatedString {
                    withStyle(style = SpanStyle()) {
                        append(
                            currentWeather.temperature.toString(),
                        )
                    }
                    withStyle(
                        style =
                            SpanStyle(
                                fontSize = 24.sp,
                                baselineShift = BaselineShift.Superscript,
                            ),
                    ) {
                        append("°C")
                    }
                },
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(modifier = Modifier.height(32.dp))
        WeatherAnalysis(
            modifier = Modifier.padding(16.dp),
            humidity = currentWeather.humidity.toString(),
            windSpeed = currentWeather.wind.toString(),
            feelsLike = currentWeather.feelsLike.toString(),
        )
    }
}

@Composable
fun HomeScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun WeatherAnalysis(
    modifier: Modifier = Modifier,
    humidity: String = "",
    windSpeed: String = "",
    feelsLike: String = "",
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.humidity),
                contentDescription = "humidity",
            )
            Text(
                text = "HUMIDITY",
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = "$humidity %",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.wind),
                contentDescription = "wind",
            )
            Text(
                text = "WIND",
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = "$windSpeed km/h",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.temp),
                contentDescription = "temp",
            )
            Text(
                text = "FEELS LIKE",
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = "$feelsLike°",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
