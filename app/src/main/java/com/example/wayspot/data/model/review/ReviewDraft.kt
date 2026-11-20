package com.example.wayspot.data.model.review

import com.example.wayspot.data.model.place.PlaceCoordinates

/** Borrador inmutable de una reseña antes de aplicar las reglas de publicación. */
data class ReviewDraft(
    val placeId: String?,
    val coordinates: PlaceCoordinates? = null,
    val rating: Int,
    val title: String,
    val description: String,
    val photoUris: List<String>,
)
