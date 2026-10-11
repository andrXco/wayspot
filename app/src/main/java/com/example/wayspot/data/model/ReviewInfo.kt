package com.example.wayspot.data.model

data class ReviewInfo(
    val id: String,
    val rating: Int,
    val title: String?,
    val description: String,
    val userId: String,
    val placeId: String,
    val reviewDate: String? = null,
    val authorName: String? = null
)
