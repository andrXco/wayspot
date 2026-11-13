package com.example.wayspot.data.model.place

/** Valores iniciales compartidos por la cámara del mapa de lugares. */
object PlacesMapDefaults {
    val initialCenter = PlaceCoordinates(latitude = 4.7110, longitude = -74.0721)

    const val initialZoom = 5.5f
    const val selectedPlaceZoom = 14.5f
    const val nearbySearchRadiusMeters = 50000
}
