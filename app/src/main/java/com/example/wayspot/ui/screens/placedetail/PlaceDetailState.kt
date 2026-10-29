package com.example.wayspot.ui.screens.placedetail

import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.review.ReviewInfo

data class PlaceDetailState(
    val place: PlaceInfo? = null,
    val fallbackPlace: Place? = null,
    val reviews: List<ReviewInfo> = emptyList()
)
