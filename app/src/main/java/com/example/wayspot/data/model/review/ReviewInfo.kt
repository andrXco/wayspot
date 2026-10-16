package com.example.wayspot.data.model.review

data class ReviewInfo(
    val id: String,
    val rating: Int,
    val title: String?,
    val description: String,
    val userId: String,
    val placeId: String,
    val reviewDate: String? = null,
    val authorName: String? = null,
    val authorUsername: String? = null,
    val authorAvatarUrl: String? = null
)
