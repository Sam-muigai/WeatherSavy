package com.samkt.weathersavy.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CurrentWeatherDatastoreImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : CurrentWeatherDataStore {
        override fun getUserLongitude(): Flow<String> {
            return context.dataStore.data.map {
                it[LONGITUDE_KEY] ?: "0.0"
            }
        }

        override suspend fun saveLongitude(longitude: String) {
            context.dataStore.edit {
                it[LONGITUDE_KEY] = longitude
            }
        }

        override fun getUserLatitude(): Flow<String> {
            return context.dataStore.data.map {
                it[LATITUDE_KEY] ?: "0.0"
            }
        }

        override suspend fun saveLatitude(latitude: String) {
            context.dataStore.edit {
                it[LATITUDE_KEY] = latitude
            }
        }

        override suspend fun saveIsOnBoardingDone(isDone: Boolean) {
            context.dataStore.edit {
                it[IS_ONBOARDING_DONE] = isDone
            }
        }

        override fun getIsOnBoardingDone(): Flow<Boolean> {
            return context.dataStore.data.map {
                it[IS_ONBOARDING_DONE] ?: false
            }
        }

        companion object {
            val LATITUDE_KEY = stringPreferencesKey("latitude")
            val LONGITUDE_KEY = stringPreferencesKey("longitude")
            val IS_ONBOARDING_DONE = booleanPreferencesKey("onBoardingDone")
        }
    }
