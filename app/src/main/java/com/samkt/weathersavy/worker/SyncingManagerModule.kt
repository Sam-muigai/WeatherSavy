package com.samkt.weathersavy.worker

import com.samkt.weathersavy.features.weather.domain.SyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncingManagerModule {
    @Binds
    @Singleton
    abstract fun bindSyncingManager(syncingManager: SyncingManager): SyncManager
}
