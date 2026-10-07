package com.example.wayspot.data.dto

import com.example.wayspot.data.model.ReviewInfo

data class ReviewDto(
    val id: Int,
    val rating: Int,
    val title: String,
    val description: String,
    val userId: Int,
    val placeId: Int,
    val commentCount: Int = 0
)

data class SaveReviewDto(
    val rating: Int,
    val title: String,
    val description: String,
    val userId: Int,
    val placeId: Int
)

fun ReviewDto.toReviewInfo(): ReviewInfo {
    return ReviewInfo(
        id = id.toString(),
        rating = rating,
        title = title,
        description = description,
        userId = userId.toString(),
        placeId = placeId.toString(),
        commentCount = commentCount
    )
}
