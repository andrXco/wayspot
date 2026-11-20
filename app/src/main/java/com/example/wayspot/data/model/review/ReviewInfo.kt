package com.example.wayspot.data.model.review

import com.example.wayspot.data.model.place.PlaceCoordinates

data class ReviewInfo(
    val id: String,
    val rating: Int,
    val title: String?,
    val description: String,
    val userId: String,
    val placeId: String?,
    val coordinates: PlaceCoordinates? = null,
    val reviewDate: String? = null,
    val authorName: String? = null,
    val authorUsername: String? = null,
    val authorAvatarUrl: String? = null,
    val likesCount: Int = 0,
    val liked: Boolean = false,
    val createdAtMillis: Long? = null,
    val photoUrls: List<String> = emptyList()

)
