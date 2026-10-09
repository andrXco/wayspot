package com.example.wayspot.data.model

data class ReviewComment(
    val id: String,
    val content: String,
    val reviewId: String,
    val author: UserInfo
)