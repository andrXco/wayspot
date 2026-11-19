package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.NearbyPlacesRemoteDataSource
import com.example.wayspot.data.model.place.NearbyPlace
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class NearbyPlacesRepository @Inject constructor(
    private val nearbyPlacesRemoteDataSource: NearbyPlacesRemoteDataSource
) {
    suspend fun findNearbyPlaces(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int
    ): Result<List<NearbyPlace>> = try {
        val nearbyPlaces = nearbyPlacesRemoteDataSource.findNearbyPlaces(
            latitude = latitude,
            longitude = longitude,
            radiusMeters = radiusMeters
        ).map { nearbyPlaceDto ->
            NearbyPlace(
                place = nearbyPlaceDto.place.toPlaceInfo(),
                distanceMeters = nearbyPlaceDto.distanceMeters
            )
        }

        Result.success(nearbyPlaces)
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}
