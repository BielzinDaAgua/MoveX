package com.example.movex.telas

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.movex.services.getRouteFromGoogleMaps
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.launch
import com.example.movex.utils.*
import com.google.android.gms.maps.MapView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.mapsplatform.transportation.consumer.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

@Composable
fun RunningScreen(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val coroutineScope = rememberCoroutineScope()

    var destinationText by remember { mutableStateOf(TextFieldValue("")) }
    var destination by remember { mutableStateOf<LatLng?>(null) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var route by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var distance by remember { mutableStateOf(0.0) }

    if (!isRunning) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Digite o destino:")
            AutocompleteTextField(
                query = destinationText.text,
                onQueryChanged = { destinationText = TextFieldValue(it) },
                onPlaceSelected = { latLng ->
                    destination = latLng
                    isRunning = true
                    startTime = System.currentTimeMillis()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        destination = geocodeDestination(context, destinationText.text)
                        if (destination != null) {
                            isRunning = true
                            startTime = System.currentTimeMillis()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Corrida")
            }
        }
    } else {
        LaunchedEffect(Unit) {
            try {
                val locationResult = fusedLocationClient.lastLocation.await()
                if (locationResult != null) {
                    currentLocation = LatLng(locationResult.latitude, locationResult.longitude)
                    coroutineScope.launch {
                        route = getRouteFromGoogleMaps(currentLocation!!, destination!!)
                    }
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }

        GoogleMapView(
            currentLocation = currentLocation,
            route = route,
            destination = destination,
            distance = distance,
            onStopRunning = {
                isRunning = false
            },
            onUpdateDistance = { newDistance ->
                distance = newDistance
            }
        )

        Column {
            Text("Tempo: ${(System.currentTimeMillis() - startTime) / 1000} segundos")
            Text("Distância: $distance metros")
            Text("Calorias: ${calculateCalories(distance)} kcal")
            Button(onClick = { isRunning = false }) {
                Text("Parar Corrida")
            }
        }
    }
}

// Adicione uma extensão para obter a localização
private fun Task<Location>.await(): Location? {
    return Tasks.await(this)
}


@Composable
fun GoogleMapView(
    currentLocation: LatLng?,
    route: List<LatLng>,
    destination: LatLng?,
    distance: Double,
    onStopRunning: () -> Unit,
    onUpdateDistance: (Double) -> Unit
) {
    val mapView = rememberMapViewWithLifecycle()

    AndroidView({ mapView }) { mapView ->
        mapView.getMapAsync { googleMap ->
            if (currentLocation != null) {
                val startLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                googleMap.addMarker(MarkerOptions().position(startLatLng).title("Início"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 15f))

                if (route.isNotEmpty()) {
                    val polylineOptions = PolylineOptions().addAll(route)
                    googleMap.addPolyline(polylineOptions)
                }
                if (destination != null && hasReachedDestination(currentLocation, destination)) {
                    onStopRunning()
                }
                if (destination != null) {
                    val totalDistance = calculateDistance(route)
                    onUpdateDistance(totalDistance)
                }
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val lifecycleObserver = getMapLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            mapView.onDestroy()
        }
    }

    return mapView
}

private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver {
    return LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> Unit
        }
    }
}

suspend fun geocodeDestination(context: android.content.Context, destinationText: String): LatLng? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(destinationText, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                LatLng(address.latitude, address.longitude)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
