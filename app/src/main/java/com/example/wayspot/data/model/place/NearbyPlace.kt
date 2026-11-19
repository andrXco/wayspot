package com.example.wayspot.data.model.place

/** Lugar encontrado dentro del radio consultado desde el centro del mapa. */
data class NearbyPlace(
    val place: PlaceInfo,
    val distanceMeters: Double
)
