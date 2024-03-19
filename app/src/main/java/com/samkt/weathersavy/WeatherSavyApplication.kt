package com.samkt.weathersavy

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.utils.NotificationHelper
import com.samkt.weathersavy.worker.SyncingWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WeatherSavyApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: CustomWorkerFactory

    override val workManagerConfiguration: Configuration
        get() =
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
}

class CustomWorkerFactory
    @Inject
    constructor(
        private val currentWeatherRepository: CurrentWeatherRepository,
        private val notificationHelper: NotificationHelper,
    ) : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters,
        ): ListenableWorker {
            return SyncingWorker(
                appContext,
                workerParameters,
                currentWeatherRepository,
                notificationHelper,
            )
        }
    }
