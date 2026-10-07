package com.example.wayspot.ui.screens.newreview

import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.ReviewDraft

/** Estado inmutable del catálogo y la reseña que se encuentra en creación. */
data class NewReviewState(
    val places: List<Place> = emptyList(),
    val place: Place? = null,
    val reviewDraft: ReviewDraft? = null,
    val isPublishEnabled: Boolean = false
)
