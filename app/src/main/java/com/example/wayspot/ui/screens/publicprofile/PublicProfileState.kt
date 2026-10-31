package com.example.wayspot.ui.screens.publicprofile

import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.review.ReviewFeedItem

data class PublicProfileState(
    val user: UserInfo? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val reviewFeedItems: List<ReviewFeedItem> = emptyList(),
    val reviewLikeCounts: Map<String, Int> = emptyMap(),
    val likedReviewIds: Set<String> = emptySet(),
    val expandedReviewIds: Set<String> = emptySet(),
    val areReviewLikesLoaded: Boolean = false,
    val canFollow: Boolean = false,
    val isFollowing: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowActionInProgress: Boolean = false,
    val hasFollowError: Boolean = false,
    val isLoading: Boolean = false,
    val hasDataLoadError: Boolean = false,
    val errorMessage: String? = null
)
