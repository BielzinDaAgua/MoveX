package com.example.movex.telas

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

@Composable
fun TrackRun(context: Context, fusedLocationClient: FusedLocationProviderClient, destination: LatLng) {
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableLongStateOf(0L) }
    var currentTime by remember { mutableLongStateOf(0L) }
    var distance by remember { mutableDoubleStateOf(0.0) }
    var currentLocation: LatLng? by remember { mutableStateOf(null) }
    var showPermissionSnackbar by remember { mutableStateOf(false) }

    // Define o callback da localização
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val newLocation = locationResult.lastLocation

            // Verificar se newLocation não é nulo
            if (newLocation != null) {
                val newLatLng = LatLng(newLocation.latitude, newLocation.longitude)

                // Atualizar distância percorrida
                if (currentLocation != null) {
                    distance += calculateDistance(currentLocation!!, newLatLng)
                }

                currentLocation = newLatLng

                // Atualizar tempo
                currentTime = System.currentTimeMillis()

                // Verificar se chegou ao destino
                if (hasReachedDestination(newLatLng, destination)) {
                    isRunning = false
                    // Finalizar corrida
                }
            }
        }
    }

    // Callback de permissão de localização
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper())
            } else {
                showPermissionSnackbar = true
            }
        }
    )

    // Efeito lançado quando a corrida começa ou para
    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = System.currentTimeMillis()

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper())
            } else {
                // Lidar com a falta de permissão
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else {
            // Finaliza o cálculo de tempo e distância
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    // Botão para iniciar/parar a corrida
    Button(onClick = { isRunning = !isRunning }) {
        Text(if (isRunning) "Parar Corrida" else "Iniciar Corrida")
    }

    // Exibir dados
    Column {
        Text("Tempo: ${(currentTime - startTime) / 1000} segundos")
        Text("Distância: $distance metros")
        Text("Calorias queimadas: ${calculateCalories(distance)} kcal")
    }

    if (showPermissionSnackbar) {
        Snackbar(
            action = {
                Button(onClick = {
                    showPermissionSnackbar = false
                }) {
                    Text("OK")
                }
            }
        ) {
            Text("Permissão de localização é necessária para rastrear a corrida.")
        }
    }
}

fun calculateDistance(start: LatLng, end: LatLng): Double {
    val result = FloatArray(1)
    android.location.Location.distanceBetween(
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
    return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .setMinUpdateIntervalMillis(2000)
        .build()
}
