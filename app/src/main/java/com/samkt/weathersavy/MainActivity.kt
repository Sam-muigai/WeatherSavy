package com.samkt.weathersavy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.samkt.weathersavy.features.weather.presentation.HomeScreen
import com.samkt.weathersavy.ui.theme.WeatherSavyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSnackBar by remember {
                mutableStateOf(true)
            }

            var locationPermissionsGranted by remember {
                mutableStateOf(
                    areLocationPermissionsAlreadyGranted(),
                )
            }
            var shouldShowPermissionRationale by remember {
                mutableStateOf(
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION),
                )
            }

            var shouldDirectUserToApplicationSettings by remember {
                mutableStateOf(false)
            }

            var currentPermissionsStatus by remember {
                mutableStateOf(
                    decideCurrentPermissionStatus(
                        locationPermissionsGranted,
                        shouldShowPermissionRationale,
                    ),
                )
            }

            val locationPermissions =
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )

            val locationPermissionLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { permissions ->
                        locationPermissionsGranted =
                            permissions.values.reduce { acc, isPermissionGranted ->
                                acc && isPermissionGranted
                            }

                        if (!locationPermissionsGranted) {
                            shouldShowPermissionRationale =
                                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                        }

                        if (locationPermissionsGranted) {
                            showSnackBar = false
                        }
                        shouldDirectUserToApplicationSettings =
                            !shouldShowPermissionRationale && !locationPermissionsGranted
                        currentPermissionsStatus =
                            decideCurrentPermissionStatus(
                                locationPermissionsGranted,
                                shouldShowPermissionRationale,
                            )
                    },
                )

            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(
                key1 = lifecycleOwner,
                effect = {
                    val observer =
                        LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_START &&
                                !locationPermissionsGranted &&
                                !shouldShowPermissionRationale
                            ) {
                                locationPermissionLauncher.launch(locationPermissions)
                            }
                        }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                },
            )
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            WeatherSavyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }) { contentPadding ->
                        if (locationPermissionsGranted) {
                            HomeScreen(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(paddingValues = contentPadding),
                            )
                        }
                    }
                    if (shouldShowPermissionRationale && showSnackBar) {
                        LaunchedEffect(Unit) {
                            scope.launch {
                                val userAction =
                                    snackbarHostState.showSnackbar(
                                        message = "Please authorize location permissions",
                                        actionLabel = "Approve",
                                        duration = SnackbarDuration.Indefinite,
                                        withDismissAction = true,
                                    )
                                when (userAction) {
                                    SnackbarResult.ActionPerformed -> {
                                        shouldShowPermissionRationale = false
                                        locationPermissionLauncher.launch(locationPermissions)
                                    }

                                    SnackbarResult.Dismissed -> {
                                        showSnackBar = false
                                    }
                                }
                            }
                        }
                    }

                    if (shouldShowPermissionRationale) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                DeniedPermission(
                                    modifier = Modifier.size(250.dp),
                                )
                                Text(
                                    text = "Cant access location without permission!!",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center,
                                )
                                Button(
                                    shape = RoundedCornerShape(8.dp),
                                    onClick = {
                                        scope.launch {
                                            shouldShowPermissionRationale = false
                                            locationPermissionLauncher.launch(locationPermissions)
                                        }
                                    },
                                ) {
                                    Text(text = "GRANT PERMISSION")
                                }
                            }
                        }
                    }

                    if (shouldDirectUserToApplicationSettings) {
                        openApplicationSettings()
                    }
                }
            }
        }
    }

    private fun areLocationPermissionsAlreadyGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openApplicationSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null),
        ).also {
            startActivity(it)
        }
    }

    private fun decideCurrentPermissionStatus(
        locationPermissionsGranted: Boolean,
        shouldShowPermissionRationale: Boolean,
    ): String {
        return if (locationPermissionsGranted) {
            "Granted"
        } else if (shouldShowPermissionRationale) {
            "Rejected"
        } else {
            "Denied"
        }
    }
}

@Composable
fun DeniedPermission(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.denied,
        ),
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
    )

    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier,
    )
}
