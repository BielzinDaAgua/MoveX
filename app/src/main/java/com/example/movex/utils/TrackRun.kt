package com.example.movex.utils

import android.Manifest
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

@Composable
fun TrackRun(fusedLocationClient: FusedLocationProviderClient, destination: LatLng) {
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var currentTime by remember { mutableStateOf(0L) }
    var distance by remember { mutableStateOf(0.0) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = System.currentTimeMillis()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.requestLocationUpdates(createLocationRequest(), object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val newLocation = locationResult.lastLocation
                    val newLatLng = LatLng(newLocation.latitude, newLocation.longitude)

                    // Atualizar distância percorrida
                    distance += calculateDistance(currentLocation!!, newLatLng)

                    // Atualizar tempo
                    currentTime = System.currentTimeMillis()

                    // Verificar se chegou ao destino
                    if (hasReachedDestination(newLatLng, destination)) {
                        isRunning = false
                        // Finalizar corrida
                    }
                }
            }, Looper.getMainLooper())
        } else {
            // Finaliza o cálculo de tempo e distância
            fusedLocationClient.removeLocationUpdates(object : LocationCallback() {})
        }
    }

    Button(onClick = { isRunning = !isRunning }) {
        Text(if (isRunning) "Parar Corrida" else "Iniciar Corrida")
    }

    // Exibir dados
    Column {
        Text("Tempo: ${(currentTime - startTime) / 1000} segundos")
        Text("Distância: $distance metros")
        Text("Calorias queimadas: ${calculateCalories(distance)} kcal")
    }
}

fun calculateDistance(start: LatLng, end: LatLng): Double {
    val result = FloatArray(1)
    ExerciseRoute.Location.distanceBetween(
        start.latitude, start.longitude,
        end.latitude, end.longitude,
        result
    )
    return result[0].toDouble()
}

fun calculateCalories(distance: Double): Double {
    // Supondo um gasto calórico médio de 0,1 kcal por metro para corrida
    return distance * 0.1
}

fun hasReachedDestination(currentLocation: LatLng, destination: LatLng): Boolean {
    // Verifica se a distância entre a localização atual e o destino é suficientemente pequena para considerar a chegada
    return calculateDistance(currentLocation, destination) < 10
}

fun createLocationRequest(): LocationRequest {
    return LocationRequest.create().apply {
        interval = 5000
        fastestInterval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}
