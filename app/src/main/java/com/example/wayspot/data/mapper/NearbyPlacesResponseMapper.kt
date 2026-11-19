package com.example.wayspot.data.mapper

import com.example.wayspot.data.dto.NearbyPlaceDto
import com.example.wayspot.data.dto.PlaceFirestoreDto
import com.example.wayspot.data.model.place.NearbyPlacesResponseException
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.google.firebase.firestore.GeoPoint

internal fun decodeNearbyPlaces(data: Any?): List<NearbyPlaceDto> {
    val response = data as? Map<*, *>
        ?: throw NearbyPlacesResponseException("La respuesta debe ser un objeto")
    val places = response["places"] as? List<*>
        ?: throw NearbyPlacesResponseException("La respuesta no contiene una lista de lugares")

    return places.mapIndexed { index, value ->
        val row = value as? Map<*, *> ?: invalidPlace(index)
        val id = (row["id"] as? String)?.takeIf { it.isNotBlank() } ?: invalidPlace(index)
        val point = row["coordinates"] as? Map<*, *> ?: invalidPlace(index)
        val latitude = (point["latitude"] as? Number)?.toDouble() ?: invalidPlace(index)
        val longitude = (point["longitude"] as? Number)?.toDouble() ?: invalidPlace(index)
        val coordinates = PlaceCoordinates(latitude, longitude)
        val distance = (row["distanceMeters"] as? Number)?.toDouble()
            ?.takeIf { it.isFinite() && it >= 0.0 } ?: invalidPlace(index)
        if (!coordinates.isValid) invalidPlace(index)

        NearbyPlaceDto(
            place = PlaceFirestoreDto(
                id = id,
                title = row["title"] as? String ?: "",
                category = row["category"] as? String ?: "",
                location = row["location"] as? String ?: "",
                rating = (row["rating"] as? Number)?.toDouble() ?: 0.0,
                imageUrl = row["imageUrl"] as? String,
                description = row["description"] as? String,
                reviewCount = (row["reviewCount"] as? Number)?.toLong() ?: 0L,
                coordinates = GeoPoint(coordinates.latitude, coordinates.longitude)
            ),
            distanceMeters = distance
        )
    }
}

private fun invalidPlace(index: Int): Nothing =
    throw NearbyPlacesResponseException("Estructura inválida en el lugar de índice $index")
