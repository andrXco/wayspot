package com.example.wayspot.data.model

import androidx.annotation.StringRes

/** Lugar navegable con recursos localizados, imagen y detalle asociado. */
data class Place(
    val id: String,
    @param:StringRes val tituloRes: Int,
    @param:StringRes val categoriaRes: Int,
    @param:StringRes val ubicacionRes: Int,
    val rating: Double = 5.0,
    val imagen: String? = null,
    val detail: PlaceDetail
)

/** Información ampliada de un lugar y sus reseñas recientes. */
data class PlaceDetail(
    @param:StringRes val durationRes: Int,
    @param:StringRes val priceRes: Int,
    @param:StringRes val altitudeRes: Int,
    @param:StringRes val descriptionRes: Int,
    val reviewCount: Int,
    val recentReviews: List<PlaceReview>
)

/** Reseña localizada incluida en el detalle de un lugar. */
data class PlaceReview(
    @param:StringRes val userNameRes: Int,
    @param:StringRes val dateRes: Int,
    val rating: Int,
    @param:StringRes val commentRes: Int
)
