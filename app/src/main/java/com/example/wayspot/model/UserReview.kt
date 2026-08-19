package com.example.wayspot.model

data class UserReview(
    val title: String,
    val location: String,
    val date: String,
    val rating: Int,
    val comment: String,
    val image: Any? = null
)
