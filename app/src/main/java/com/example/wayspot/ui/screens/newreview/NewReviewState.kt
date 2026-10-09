package com.example.wayspot.ui.screens.newreview

import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.data.model.ReviewDraft

/** Estado inmutable del catálogo y la reseña que se encuentra en creación. */
data class NewReviewState(
    val places: List<PlaceInfo> = emptyList(),
    val place: PlaceInfo? = null,
    val reviewDraft: ReviewDraft? = null,
    val isPublishEnabled: Boolean = false,
    val editingReviewId: String? = null,
    val isLoading: Boolean = false,
    val isPublishing: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val errorResId: Int? = null
)
