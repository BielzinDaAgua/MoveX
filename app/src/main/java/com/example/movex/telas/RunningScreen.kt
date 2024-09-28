package com.example.movex.telas

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.movex.utils.calculateCalories
import com.google.android.gms.maps.model.PolylineOptions

@Composable
fun RunningScreen(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationUpdates by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var elapsedTime by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        val activity = context as? ComponentActivity
        if (activity != null) {
            checkLocationPermissions(
                activity,
                fusedLocationClient
            ) { location ->
                currentLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMapView(
            currentLocation = currentLocation,
            locationUpdates = locationUpdates
        )

        Button(onClick = { navController.navigate("main_screen/$userId") }) {
            Text("Voltar")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                ) {

                    Text(
                        text = "$elapsedTime",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        if(!isRunning) {
                            Button(onClick = {
                                isRunning = true
                                startTime = System.currentTimeMillis()
                                locationUpdates = emptyList()
                            }) {
                                Text("Iniciar Corrida")
                            }
                        } else {
                            Button(onClick = {
                                isRunning = false
                                elapsedTime = System.currentTimeMillis() - startTime
                                val calories = calculateCalories(locationUpdates, elapsedTime)
                                Toast.makeText(
                                    context,
                                    "Tempo: ${elapsedTime / 1000} segs, Calorias: $calories",
                                    Toast.LENGTH_LONG
                                ).show()
                            }) {
                                Text("Parar Corrida")
                            }
                        }
                    }
                }
            }
        }
    }

    if (isRunning) {
        LaunchedEffect(Unit) {
            val activity = context as? ComponentActivity
            if (activity != null) {
                checkLocationPermissions(activity, fusedLocationClient) { location ->
                    val newLocation = LatLng(location.latitude, location.longitude)
                    currentLocation = newLocation
                    locationUpdates = locationUpdates + newLocation
                }
            }
        }
    }
}

@Composable
fun GoogleMapView(currentLocation: LatLng?, locationUpdates: List<LatLng>) {
    val mapView = rememberMapViewWithLifecycle()

    AndroidView({ mapView }) { mapView ->
        mapView.getMapAsync { googleMap ->
            if (currentLocation != null) {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(currentLocation)
                        .title("Você está aqui")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

                if (locationUpdates.isNotEmpty()) {
                    val polylineOptions = PolylineOptions().addAll(locationUpdates)
                    googleMap.addPolyline(polylineOptions)
                }
            }
        }
    }
}

private fun checkLocationPermissions(
    activity: ComponentActivity,
    fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
    onLocationFound: (Location) -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationFound(location)
            }
        }
    } else {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
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

private const val LOCATION_PERMISSION_REQUEST_CODE = 1
