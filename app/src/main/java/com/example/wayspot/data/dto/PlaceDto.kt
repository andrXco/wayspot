package com.example.wayspot.data.dto
import com.example.wayspot.data.model.PlaceInfo

data class PlaceDto(
    val id: Int,
    val title: String,
    val category: String,
    val location: String,
    val rating: Double,
    val imageUrl: String?,
    val description: String
)

fun PlaceDto.toPlaceInfo(): PlaceInfo {
    return PlaceInfo(
        id = id.toString(),
        title = title,
        category = category,
        location = location,
        rating = rating,
        imageUrl = imageUrl,
        description = description
    )
}