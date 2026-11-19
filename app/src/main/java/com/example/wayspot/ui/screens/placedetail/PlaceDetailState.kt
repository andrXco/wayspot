package com.example.wayspot.ui.screens.placedetail

import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.ui.components.map.MapRenderState

enum class PlaceDetailLoadStatus {
    Loading,
    Ready,
    Failed
}

data class PlaceDetailState(
    val place: PlaceInfo? = null,
    val placeStatus: PlaceDetailLoadStatus = PlaceDetailLoadStatus.Loading,
    val reviews: List<ReviewInfo> = emptyList(),
    val reviewsStatus: PlaceDetailLoadStatus = PlaceDetailLoadStatus.Loading,
    val mapRenderState: MapRenderState = MapRenderState()
)
