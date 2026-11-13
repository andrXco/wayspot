package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.datasource.NearbyPlacesRemoteDataSource
import com.example.wayspot.data.dto.NearbyPlaceDto
import com.example.wayspot.data.dto.PlaceFirestoreDto
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.functions.FirebaseFunctions
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
        val response = functions
            .getHttpsCallable(FIND_NEARBY_PLACES_FUNCTION)
            .call(
                mapOf(
                    "latitude" to latitude,
                    "longitude" to longitude,
                    "radiusMeters" to radiusMeters
                )
            )
            .await()

        val responseData = response.data as? Map<*, *>
            ?: throw IllegalStateException("La búsqueda cercana devolvió una respuesta inválida")
        val places = responseData[PLACES_RESPONSE_KEY] as? List<*>
            ?: throw IllegalStateException("La búsqueda cercana no devolvió la lista de lugares")

        return places.mapNotNull { rawPlace ->
            val placeData = rawPlace as? Map<*, *> ?: return@mapNotNull null
            val place = placeData.toPlaceFirestoreDto() ?: return@mapNotNull null
            val distanceMeters = (placeData[DISTANCE_RESPONSE_KEY] as? Number)?.toDouble()
                ?: return@mapNotNull null

            NearbyPlaceDto(
                place = place,
                distanceMeters = distanceMeters
            )
        }
    }

    private fun Map<*, *>.toPlaceFirestoreDto(): PlaceFirestoreDto? {
        val id = this[ID_RESPONSE_KEY] as? String ?: return null
        val coordinatesData = this[COORDINATES_RESPONSE_KEY] as? Map<*, *> ?: return null
        val latitude = (coordinatesData[LATITUDE_RESPONSE_KEY] as? Number)?.toDouble()
            ?: return null
        val longitude = (coordinatesData[LONGITUDE_RESPONSE_KEY] as? Number)?.toDouble()
            ?: return null

        return PlaceFirestoreDto(
            id = id,
            title = this[TITLE_RESPONSE_KEY] as? String ?: "",
            category = this[CATEGORY_RESPONSE_KEY] as? String ?: "",
            location = this[LOCATION_RESPONSE_KEY] as? String ?: "",
            rating = (this[RATING_RESPONSE_KEY] as? Number)?.toDouble() ?: 0.0,
            imageUrl = this[IMAGE_URL_RESPONSE_KEY] as? String,
            description = this[DESCRIPTION_RESPONSE_KEY] as? String,
            reviewCount = (this[REVIEW_COUNT_RESPONSE_KEY] as? Number)?.toLong() ?: 0L,
            coordinates = GeoPoint(latitude, longitude)
        )
    }

    private companion object {
        const val FIND_NEARBY_PLACES_FUNCTION = "findNearbyPlaces"
        const val PLACES_RESPONSE_KEY = "places"
        const val DISTANCE_RESPONSE_KEY = "distanceMeters"
        const val ID_RESPONSE_KEY = "id"
        const val TITLE_RESPONSE_KEY = "title"
        const val CATEGORY_RESPONSE_KEY = "category"
        const val LOCATION_RESPONSE_KEY = "location"
        const val RATING_RESPONSE_KEY = "rating"
        const val IMAGE_URL_RESPONSE_KEY = "imageUrl"
        const val DESCRIPTION_RESPONSE_KEY = "description"
        const val REVIEW_COUNT_RESPONSE_KEY = "reviewCount"
        const val COORDINATES_RESPONSE_KEY = "coordinates"
        const val LATITUDE_RESPONSE_KEY = "latitude"
        const val LONGITUDE_RESPONSE_KEY = "longitude"
    }
}
