package com.example.movex.utils

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import androidx.compose.ui.platform.LocalContext

@Composable
fun AutocompleteTextField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onPlaceSelected: (LatLng) -> Unit
) {
    var suggestions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var isSuggestionsVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    TextField(
        value = query,
        onValueChange = {
            onQueryChanged(it)
            if (it.isNotEmpty()) {
                fetchAutocompleteSuggestions(it, context) { newSuggestions ->
                    suggestions = newSuggestions
                    isSuggestionsVisible = newSuggestions.isNotEmpty()
                }
            } else {
                suggestions = emptyList()
                isSuggestionsVisible = false
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (isSuggestionsVisible) {
        SuggestionList(suggestions) { selectedPlace ->
            selectedPlace.placeId?.let { placeId ->
                fetchPlaceDetails(placeId, context) { latLng ->
                    onPlaceSelected(latLng)
                    onQueryChanged(selectedPlace.getPrimaryText(null).toString())
                    suggestions = emptyList()
                    isSuggestionsVisible = false
                }
            }
        }
    }
}

@Composable
fun SuggestionList(
    suggestions: List<AutocompletePrediction>,
    onPlaceSelected: (AutocompletePrediction) -> Unit
) {
    Column {
        suggestions.forEach { place ->
            Text(
                text = place.getPrimaryText(null).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlaceSelected(place) }
                    .padding(8.dp)
            )
        }
    }
}

private fun fetchAutocompleteSuggestions(query: String, context: Context, callback: (List<AutocompletePrediction>) -> Unit) {
    val placesClient = Places.createClient(context)
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            val predictions = response.autocompletePredictions
            callback(predictions)
        }
        .addOnFailureListener { exception ->
            exception.printStackTrace()
            callback(emptyList())
        }
}

private fun fetchPlaceDetails(placeId: String, context: Context, callback: (LatLng) -> Unit) {
    val placesClient = Places.createClient(context)

    // Specify the fields to fetch
    val placeFields = listOf(Place.Field.LAT_LNG)

    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response ->
            val place = response.place
            place.latLng?.let {
                callback(it)
            } ?: run {
                // Handle the case where latLng is null
                callback(LatLng(0.0, 0.0)) // Retorne uma localização padrão ou um erro
            }
        }
        .addOnFailureListener { exception ->
            exception.printStackTrace()
            callback(LatLng(0.0, 0.0)) // Retorne uma localização padrão ou um erro
        }
}
