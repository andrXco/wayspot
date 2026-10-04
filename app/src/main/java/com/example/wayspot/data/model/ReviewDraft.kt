package com.example.wayspot.data.model

/** Borrador inmutable de una reseña antes de aplicar las reglas de publicación. */
data class ReviewDraft(
    val placeId: String,
    val rating: Int,
    val title: String,
    val description: String,
    val photoUris: List<String>,
)
