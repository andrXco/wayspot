package com.example.wayspot.ui.screens.reviewdetail

import com.example.wayspot.data.model.UserInfo
import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.data.model.ReviewComment
import com.example.wayspot.data.model.ReviewInfo

data class ReviewDetailState(
    val review: ReviewInfo? = null,
    val place: PlaceInfo? = null,
    val author: UserInfo? = null,
    val comments: List<ReviewComment> = emptyList(),
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null
)
