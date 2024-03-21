package com.samkt.weathersavy.di

import com.samkt.weathersavy.core.datastore.CurrentWeatherDataStore
import com.samkt.weathersavy.core.datastore.CurrentWeatherDatastoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Binds
    @Singleton
    abstract fun bindCurrentWeatherModule(currentWeatherDatastoreImpl: CurrentWeatherDatastoreImpl): CurrentWeatherDataStore
}
