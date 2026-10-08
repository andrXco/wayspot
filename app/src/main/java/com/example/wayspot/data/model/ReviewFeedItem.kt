package com.example.wayspot.data.model

import com.example.wayspot.data.dto.UserDto

data class ReviewFeedItem(
    val review: ReviewInfo,
    val place: PlaceInfo,
    val author: UserDto
)
