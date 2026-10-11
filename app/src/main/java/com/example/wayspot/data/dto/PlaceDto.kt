package com.example.wayspot.data.dto

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
)
