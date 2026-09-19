package com.example.wayspot.ui.screens.newreview

import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.ReviewDraft

/** Estado inmutable de una reseña en creación para un lugar determinado. */
data class NewReviewState(
    val place: Place? = null,
    val reviewDraft: ReviewDraft? = null,
    val isPublishEnabled: Boolean = false
)
