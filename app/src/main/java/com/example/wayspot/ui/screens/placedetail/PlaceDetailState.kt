package com.example.wayspot.ui.screens.placedetail

import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.data.model.ReviewInfo

data class PlaceDetailState(
    val place: PlaceInfo? = null,
    val reviews: List<ReviewInfo> = emptyList()
)