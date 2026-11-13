package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.NearbyPlaceDto

interface NearbyPlacesRemoteDataSource {
    suspend fun findNearbyPlaces(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int
    ): List<NearbyPlaceDto>
}
