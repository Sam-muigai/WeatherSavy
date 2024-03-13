package com.samkt.weathersavy.di

import android.content.Context
import androidx.room.Room
import com.samkt.weathersavy.core.database.CurrentWeatherDatabase
import com.samkt.weathersavy.core.network.OpenWeatherApiService
import com.samkt.weathersavy.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOpenWeatherApiService(): OpenWeatherApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(OpenWeatherApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrentWeatherDatabase(
        @ApplicationContext context: Context,
    ): CurrentWeatherDatabase {
        return Room.databaseBuilder(context, CurrentWeatherDatabase::class.java, "weather_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
