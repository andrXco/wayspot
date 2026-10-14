package com.example.wayspot.data.dto

import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.place.PlaceDetail
import com.example.wayspot.data.model.place.PlaceInfo

data class PlaceDto(
    val id: Int,
    val slug: String? = null,
    val title: String,
    val category: String,
    val location: String,
    val rating: Double,
    val imageUrl: String?,
    val description: String?,
    val duration: String? = null,
    val price: String? = null,
    val altitude: String? = null,
    val reviewCount: Int = 0
) : PlaceDtoGeneric() {

    override fun toPlaceInfo(): PlaceInfo {
        return PlaceInfo(
            id = id.toString(),
            title = title,
            category = category,
            location = location,
            rating = rating,
            imageUrl = imageUrl,
            description = description ?: "",
            slug = slug ?: id.toString(),
            duration = duration,
            price = price,
            altitude = altitude,
            reviewCount = reviewCount
        )
    }

    override fun toPlace(): Place {
        return Place(
            id = slug ?: id.toString(),
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
                reviewCount = reviewCount,
                recentReviews = emptyList(),
                durationText = duration,
                priceText = price,
                altitudeText = altitude,
                descriptionText = description
            ),
            titleText = title,
            categoryText = category,
            locationText = location
        )
    }

}
