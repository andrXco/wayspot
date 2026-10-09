package com.example.wayspot.data.model

import com.example.wayspot.data.dto.UserDto

data class ReviewComment(
    val id: String,
    val content: String,
    val reviewId: String,
    val author: UserDto
)
