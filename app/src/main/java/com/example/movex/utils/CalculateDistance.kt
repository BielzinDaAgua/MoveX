package com.example.movex.utils

import com.google.android.gms.maps.model.LatLng

fun calculateDistance(route: List<LatLng>): Double {
    var totalDistance = 0.0
    for (i in 0 until route.size - 1) {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            route[i].latitude, route[i].longitude,
            route[i + 1].latitude, route[i + 1].longitude,
            results
        )
        totalDistance += results[0]
    }
    return totalDistance
}