package com.samkt.weathersavy.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.samkt.weathersavy.core.location.model.UserLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "LocationService"

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
                Log.d(TAG, "Location: $location")
                val latitude = location.latitude
                val longitude = location.longitude
                val userLocation = UserLocation(longitude.toString(), latitude.toString())
                userLocation
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.d(TAG, it) }
                null
            }
        }
    }
