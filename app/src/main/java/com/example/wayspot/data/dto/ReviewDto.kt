package com.example.wayspot.data.dto

data class ReviewDto(
    val id: Int,
    val rating: Int,
    val title: String?,
    val description: String,
    val userId: Int,
    val placeId: Int,
    val reviewDate: String? = null,
    val user: ReviewUserDto? = null
)

data class ReviewUserDto(val name: String?)

data class SaveReviewDto(
    val rating: Int,
    val title: String,
    val description: String,
    val userId: Int,
    val placeId: Int
)
