package com.example.movex.telas

import android.Manifest
import android.content.Intent
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.delay

@Composable
fun RunningScreen(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationUpdates by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var summary by remember { mutableStateOf("") }
    val testMode = false

    LaunchedEffect(Unit) {
        if (testMode) {
            // Simula uma lista de pontos GPS em uma rota
            locationUpdates = listOf(
                LatLng(-23.550520, -46.633308), // São Paulo
                LatLng(-22.906847, -43.172897), // Rio de Janeiro
                LatLng(-19.916681, -43.934493)  // Belo Horizonte
            )
        } else {
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
    }

    if (isRunning) {
        LaunchedEffect(Unit) {
            while (isRunning) {
                delay(1000)
                elapsedTime = System.currentTimeMillis() - startTime
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMapView(
            currentLocation = currentLocation,
            locationUpdates = locationUpdates
        )

        Button(onClick = { navController.navigate("main_screen/$userId") },
            modifier = Modifier.padding(16.dp), colors =  ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA500)
            )) {
            Text("Voltar")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp)
                .align(Alignment.TopCenter),
        ) {

            Spacer(modifier = Modifier.height(100.dp))

            if (!isRunning && summary.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(320.dp)
                        .padding(vertical = 2.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Parabéns!",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = summary,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, summary)
                                        type = "text/plain"
                                    }
                                    context.startActivity(
                                        Intent.createChooser(
                                            shareIntent,
                                            "Compartilhar corrida"
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFA500)
                                )
                            ) {
                                Text("Compartilhe sua conquista", color = Color.Black)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    summary = ""
                                    elapsedTime = 0
                                },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black
                                )
                            ) {
                                Text("Fechar", color = Color.White)
                            }
                        }
                    }
                }
            }
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
                    .size(220.dp)
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ){
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        val seconds = (elapsedTime / 1000) % 60
                        val minutes = (elapsedTime / 1000) / 60
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            style = TextStyle(
                                fontSize = 50.sp,
                            ),
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        if (!isRunning) {
                            Button(onClick = {
                                isRunning = true
                                startTime = System.currentTimeMillis()
                                locationUpdates = emptyList()
                                summary = ""
                            }, modifier = Modifier.width(200.dp)
                                .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFA500)
                                    )) {
                                Text("Iniciar Corrida", color = Color.Black)
                            }
                        } else {
                            Button(onClick = {
                                isRunning = false
                                elapsedTime = System.currentTimeMillis() - startTime
                                val distance = calculateDistance(locationUpdates)

                                summary = "Tempo: ${(elapsedTime / 1000)} segundos\nDistância: ${distance / 1000} km"
                            },  modifier = Modifier.width(200.dp)
                                .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )) {
                                Text("Parar Corrida", color = Color.White)
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

                val polylineOptions = PolylineOptions()
                    .color(android.graphics.Color.argb(255, 255, 165, 0))
                    .width(8f)
                    .addAll(locationUpdates)
                googleMap.addPolyline(polylineOptions)
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

fun calculateDistance(points: List<LatLng>): Double {
    if (points.size < 2) return 0.0

    var totalDistance = 0.0
    for (i in 0 until points.size - 1) {
        totalDistance += haversine(points[i], points[i + 1])
    }
    return totalDistance
}

fun haversine(start: LatLng, end: LatLng): Double {
    val R = 6371e3
    val lat1 = Math.toRadians(start.latitude)
    val lat2 = Math.toRadians(end.latitude)
    val dLat = lat2 - lat1
    val dLng = Math.toRadians(end.longitude - start.longitude)

    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1) * Math.cos(lat2) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return R * c
}

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

@Preview(showBackground = true)
@Composable
fun RunningScreenPreview() {
    // Simular um NavController, se necessário
    val navController = rememberNavController()
    val userId = 1 // Simule um ID de usuário

    RunningScreen(navController = navController, userId = userId)
}