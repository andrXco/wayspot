package com.example.wayspot.ui.screens.reviewdetail

import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.review.ReviewComment
import com.example.wayspot.data.model.review.ReviewInfo

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
