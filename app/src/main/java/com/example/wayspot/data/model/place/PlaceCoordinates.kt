package com.example.wayspot.data.model.place

/** Coordenadas geográficas independientes del SDK de Firebase y del SDK de Maps. */
data class PlaceCoordinates(
    val latitude: Double,
    val longitude: Double
) {
    val isValid: Boolean
        get() = latitude in MIN_LATITUDE..MAX_LATITUDE &&
            longitude in MIN_LONGITUDE..MAX_LONGITUDE

    companion object {
        private const val MIN_LATITUDE = -90.0
        private const val MAX_LATITUDE = 90.0
        private const val MIN_LONGITUDE = -180.0
        private const val MAX_LONGITUDE = 180.0
    }
}
