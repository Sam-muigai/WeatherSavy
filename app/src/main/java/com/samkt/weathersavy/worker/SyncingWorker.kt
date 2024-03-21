package com.samkt.weathersavy.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import com.samkt.weathersavy.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

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
                Log.d(TAG, "Doing sync work")
                currentWeatherRepository.syncWeather(
                    onSyncSuccess = {
                        notificationHelper.showNotification(
                            title = it.location,
                            content = it.condition,
                        )
                    },
                    onSyncFailed = { exc ->
                        exc.localizedMessage?.let { error -> Log.d(TAG, error) }
                    },
                )
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }

        companion object {
            fun startSyncing(context: Context) {
                val constraints =
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                val startSyncingRequest =
                    PeriodicWorkRequestBuilder<SyncingWorker>(
                        repeatInterval = 30,
                        repeatIntervalTimeUnit = TimeUnit.MINUTES,
                    )
                        .setConstraints(constraints)
                        .build()

                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                    startSyncingRequest,
                )
            }
        }
    }

const val WORK_NAME = "syncing_work"
