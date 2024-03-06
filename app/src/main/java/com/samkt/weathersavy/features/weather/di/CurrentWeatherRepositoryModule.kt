package com.samkt.weathersavy.features.weather.di

import com.samkt.weathersavy.features.weather.data.CurrentWeatherRepositoryImpl
import com.samkt.weathersavy.features.weather.domain.CurrentWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CurrentWeatherRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCurrentWeatherRepository(currentWeatherRepositoryImpl: CurrentWeatherRepositoryImpl): CurrentWeatherRepository
}
