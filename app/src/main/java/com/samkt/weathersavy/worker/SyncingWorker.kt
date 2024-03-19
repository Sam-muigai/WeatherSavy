package com.samkt.weathersavy.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val TAG = "SyncingWorker"

@HiltWorker
class SyncingWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted params: WorkerParameters,
        @Assisted private val currentWeatherRepository: CurrentWeatherRepository,
        @Assisted private val notificationHelper: NotificationHelper,
    ) : CoroutineWorker(appContext, params) {
        override suspend fun doWork(): Result {
            return try {
                currentWeatherRepository.syncWeather(
                    onSyncSuccess = {
                        notificationHelper.showNotification(
                            title = it.location,
                            content = it.condition,
                        )
                    },
                    onSyncFailed = {
                        notificationHelper.showNotification(
                            title = "Failed",
                            content = it.localizedMessage ?: "Unknown error",
                        )
                    },
                )
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

fun startSyncing(context: Context) {
    Log.d(TAG, "Sync starting")
    val startSyncingRequest =
        OneTimeWorkRequestBuilder<SyncingWorker>()
            .build()

    WorkManager.getInstance(context).enqueue(startSyncingRequest)
}