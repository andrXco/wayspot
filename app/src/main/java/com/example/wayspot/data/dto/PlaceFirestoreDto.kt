
package com.example.wayspot.data.dto

import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.place.PlaceDetail
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.google.firebase.firestore.GeoPoint

data class PlaceFirestoreDto(
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val location: String = "",
    val rating: Double = 0.0,
    val imageUrl: String? = null,
    val description: String? = null,
    val reviewCount: Long = 0L,
    val coordinates: GeoPoint? = null //Aqui Conexion

) : PlaceDtoGeneric() {

    override fun toPlaceInfo(): PlaceInfo {
        return PlaceInfo(
            id = id,
            title = title,
            category = category,
            location = location,
            rating = rating,
            imageUrl = imageUrl,
            description = description ?: "",
            slug = id,
            reviewCount = reviewCount.toInt(),
            coordinates = coordinates?.let { point ->
                PlaceCoordinates(
                    latitude = point.latitude,
                    longitude = point.longitude
                )
            }
        )
    }

    override fun toPlace(): Place {
        return Place(
            id = id,
            tituloRes = 0,
            categoriaRes = 0,
            ubicacionRes = 0,
            rating = rating,
            imagen = imageUrl,
            detail = PlaceDetail(
                durationRes = 0,
                priceRes = 0,
                altitudeRes = 0,
                descriptionRes = 0,
                reviewCount = reviewCount.toInt(),
                recentReviews = emptyList(),
                descriptionText = description
            ),
            titleText = title,
            categoryText = category,
            locationText = location,
            coordinates = coordinates?.let { point ->
                PlaceCoordinates(
                    latitude = point.latitude,
                    longitude = point.longitude
                )
            }
        )
    }
}
