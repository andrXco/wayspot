package com.example.wayspot.data.model.review

import com.example.wayspot.data.model.user.UserInfo

data class ReviewComment(
    val id: String,
    val content: String,
    val reviewId: String,
    val author: UserInfo
)
