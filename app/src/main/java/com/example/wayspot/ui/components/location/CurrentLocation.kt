package com.example.wayspot.ui.components.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.util.Consumer
import com.example.wayspot.data.model.place.PlaceCoordinates

fun hasLocationPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

@SuppressLint("MissingPermission")
fun requestCurrentLocation(
    context: Context,
    onLocationFound: (PlaceCoordinates) -> Unit,
    onUnavailable: () -> Unit
) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!LocationManagerCompat.isLocationEnabled(locationManager)) {
        onUnavailable()
        return
    }

    val enabledProviders = locationManager.getProviders(true)
    val hasFineLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val provider = when {
        hasFineLocationPermission && LocationManager.GPS_PROVIDER in enabledProviders ->
            LocationManager.GPS_PROVIDER
        LocationManager.NETWORK_PROVIDER in enabledProviders -> LocationManager.NETWORK_PROVIDER
        else -> null
    }

    if (provider == null) {
        onUnavailable()
        return
    }

    try {
        val locationConsumer = Consumer<Location?> { location ->
            if (location == null) {
                onUnavailable()
            } else {
                onLocationFound(
                    PlaceCoordinates(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
            }
        }
        LocationManagerCompat.getCurrentLocation(
            locationManager,
            provider,
            null as CancellationSignal?,
            ContextCompat.getMainExecutor(context),
            locationConsumer
        )
    } catch (_: SecurityException) {
        onUnavailable()
    }
}
