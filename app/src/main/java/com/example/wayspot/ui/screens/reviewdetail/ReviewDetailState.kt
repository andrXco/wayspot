package com.example.wayspot.ui.screens.reviewdetail

import com.example.wayspot.data.model.BackendUser
import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.data.model.ReviewComment
import com.example.wayspot.data.model.ReviewInfo

data class ReviewDetailState(
    val review: ReviewInfo? = null,
    val place: PlaceInfo? = null,
    val author: BackendUser? = null,
    val comments: List<ReviewComment> = emptyList(),
    val commentDraft: String = "",
    val isLoading: Boolean = false,
    val isPosting: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null
)
