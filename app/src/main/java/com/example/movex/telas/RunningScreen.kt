package com.example.movex.telas

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.MapView

@Composable
fun RunningScreen(destination: LatLng) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val coroutineScope = rememberCoroutineScope()

    // Localização atual
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var route by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    // Inicializar GoogleMapView
    GoogleMapView(
        currentLocation = currentLocation,
        route = route
    )

    // Obter a localização do usuário
    LaunchedEffect(Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    coroutineScope.launch {
                        route = getRouteFromGoogleMaps(currentLocation!!, destination) // Defina o destino
                    }
                }
            }
        } catch (e: SecurityException) {
            // Gerenciar exceção de permissão de localização
            e.printStackTrace()
        }
    }
}

@Composable
fun GoogleMapView(currentLocation: LatLng?, route: List<LatLng>) {
    val mapView = rememberMapViewWithLifecycle()

    AndroidView({ mapView }) { mapView ->
        mapView.getMapAsync { googleMap ->
            if (currentLocation != null) {
                val startLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                googleMap.addMarker(MarkerOptions().position(startLatLng).title("Starting Point"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 15f))

                if (route.isNotEmpty()) {
                    val polylineOptions = PolylineOptions().addAll(route)
                    googleMap.addPolyline(polylineOptions)
                }
            }
        }
    }
}

suspend fun getRouteFromGoogleMaps(start: LatLng, destination: LatLng): List<LatLng> {
    // Função que faz a requisição para a Google Directions API e retorna uma lista de LatLng representando a rota
    // Exemplo de uso de Retrofit ou HttpUrlConnection para fazer a chamada HTTP à API
    return listOf() // Substituir pelo cálculo da rota
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
