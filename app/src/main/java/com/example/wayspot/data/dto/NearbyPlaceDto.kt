package com.example.wayspot.data.dto

data class NearbyPlaceDto(
    val place: PlaceFirestoreDto,
    val distanceMeters: Double
)
