package com.samkt.weathersavy.core.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.samkt.weathersavy.core.location.model.UserLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationService
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private var fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        @SuppressLint("MissingPermission")
        suspend fun getLocation(): UserLocation? {
            return try {
                val location = fusedLocationClient.lastLocation.await()
                val latitude = location.latitude
                val longitude = location.longitude
                val userLocation = UserLocation(longitude.toString(), latitude.toString())
                userLocation
            } catch (e: Exception) {
                null
            }
        }
    }
