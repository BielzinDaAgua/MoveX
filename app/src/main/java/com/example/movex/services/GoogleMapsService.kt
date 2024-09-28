package com.example.movex.services

import com.example.movex.BuildConfig
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class DirectionsResponse(
    val routes: List<Route>
)

data class Route(
    val overviewPolyline: OverviewPolyline
)

data class OverviewPolyline(
    val points: String
)


interface DirectionsApiService {
    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): DirectionsResponse
}


fun createDirectionsApi(): DirectionsApiService {
    return Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DirectionsApiService::class.java)
}


fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val position = LatLng((lat / 1E5), (lng / 1E5))
        poly.add(position)
    }

    return poly
}

suspend fun getRouteFromGoogleMaps(start: LatLng, destination: LatLng): List<LatLng> {
    val directionsApi = createDirectionsApi()

    val origin = "${start.latitude},${start.longitude}"
    val dest = "${destination.latitude},${destination.longitude}"

    val apiKey = BuildConfig.GOOGLE_API_KEY

    return withContext(Dispatchers.IO) {
        try {
            val response = directionsApi.getDirections(origin, dest, apiKey)

            if (response.routes.isNotEmpty()) {
                decodePolyline(response.routes[0].overviewPolyline.points)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList<LatLng>()
        }
    }
}


