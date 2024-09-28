package com.example.movex.utils

import com.google.android.gms.maps.model.LatLng

fun hasReachedDestination(currentLocation: LatLng, destination: LatLng, thresholdInMeters: Float = 20f): Boolean {
    val results = FloatArray(1)
    android.location.Location.distanceBetween(
        currentLocation.latitude, currentLocation.longitude,
        destination.latitude, destination.longitude,
        results
    )
    return results[0] <= thresholdInMeters
}