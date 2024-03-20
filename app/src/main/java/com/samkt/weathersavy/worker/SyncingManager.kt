package com.samkt.weathersavy.worker

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.samkt.weathersavy.features.weather.domain.SyncManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncingManager
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : SyncManager {
        override val isSyncing: Flow<Boolean>
            get() =
                WorkManager.getInstance(context)
                    .getWorkInfosForUniqueWorkFlow(WORK_NAME)
                    .map {
                        it.anyRunning()
                    }
                    .conflate()
    }

private fun List<WorkInfo>.anyRunning() = any { it.state == WorkInfo.State.RUNNING }
