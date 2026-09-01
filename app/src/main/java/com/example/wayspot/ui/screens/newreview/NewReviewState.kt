package com.example.wayspot.ui.screens.newreview

data class NewReviewState(
    val rating: Int = 0,
    val title: String = "",
    val description: String = "",
    val photoUris: List<String> = emptyList()
)