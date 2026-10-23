package com.example.wayspot.data.model

data class PlaceInfo(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val rating: Double,
    val imageUrl: String?,
    val description: String
)