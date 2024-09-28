package com.example.movex.utils

fun calculateCalories(distanceInMeters: Double): Double {
    val distanceInKilometers = distanceInMeters / 1000
    val caloriesPerKilometer = 80.0
    return distanceInKilometers * caloriesPerKilometer
}
