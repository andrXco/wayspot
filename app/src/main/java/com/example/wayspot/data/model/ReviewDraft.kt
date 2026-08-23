package com.example.wayspot.data.model

data class ReviewDraft(
    val placeId: String,
    val rating: Int,
    val title: String,
    val description: String,
    val photoUris: List<String>,
)
