package com.example.wayspot.data.model

import androidx.annotation.StringRes

data class Places(
    val id: String,
    @param:StringRes val tituloRes: Int,
    @param:StringRes val categoriaRes: Int,
    @param:StringRes val ubicacionRes: Int,
    val rating: Double = 5.0,
    val imagen: Any? = null,
    val isSaved: Boolean = false,
    val detail: PlaceDetail
)

data class PlaceDetail(
    @param:StringRes val durationRes: Int,
    @param:StringRes val priceRes: Int,
    @param:StringRes val altitudeRes: Int,
    @param:StringRes val descriptionRes: Int,
    val reviewCount: Int,
    val recentReviews: List<PlaceReview>
)

data class PlaceReview(
    @param:StringRes val userNameRes: Int,
    @param:StringRes val dateRes: Int,
    val rating: Int,
    @param:StringRes val commentRes: Int
)
