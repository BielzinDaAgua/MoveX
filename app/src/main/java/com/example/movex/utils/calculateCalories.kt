package com.example.movex.utils

import com.google.android.gms.maps.model.LatLng

public fun calculateCalories(locations: List<LatLng>, timeMillis: Long): Long {
    return (timeMillis / 1000) * 0.1.toInt()
}
