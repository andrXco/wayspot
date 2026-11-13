package com.example.wayspot.ui.screens.map

import com.example.wayspot.data.model.place.PlaceInfo

/** Estado renderizable de la pantalla del mapa. */
data class PlacesMapState(
    val places: List<PlaceInfo> = emptyList(),
    val selectedPlaceId: String? = null,
    val isLoading: Boolean = true,
    val hasError: Boolean = false
) {
    val selectedPlace: PlaceInfo?
        get() = places.firstOrNull { place -> place.id == selectedPlaceId }
}
