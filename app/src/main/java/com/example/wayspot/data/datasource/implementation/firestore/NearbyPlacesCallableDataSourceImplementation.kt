package com.example.wayspot.data.datasource.implementation.firestore

import android.util.Log
import com.example.wayspot.data.datasource.NearbyPlacesRemoteDataSource
import com.example.wayspot.data.dto.NearbyPlaceDto
import com.example.wayspot.data.mapper.decodeNearbyPlaces
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class NearbyPlacesCallableDataSourceImplementation @Inject constructor(
    private val functions: FirebaseFunctions
) : NearbyPlacesRemoteDataSource {

    override suspend fun findNearbyPlaces(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int
    ): List<NearbyPlaceDto> {
        val response = try {
            functions.getHttpsCallable(FIND_NEARBY_PLACES_FUNCTION)
                .call(
                    mapOf(
                        "latitude" to latitude,
                        "longitude" to longitude,
                        "radiusMeters" to radiusMeters
                    )
                )
                .await()
        } catch (error: FirebaseFunctionsException) {
            Log.w(TAG, "Falló findNearbyPlaces con código ${error.code}")
            throw error
        }

        return decodeNearbyPlaces(response.data)
    }

    private companion object {
        const val TAG = "NearbyPlacesCallable"
        const val FIND_NEARBY_PLACES_FUNCTION = "findNearbyPlaces"
    }
}
