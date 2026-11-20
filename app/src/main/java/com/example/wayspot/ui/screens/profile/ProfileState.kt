package com.example.wayspot.ui.screens.profile

import com.example.wayspot.data.model.achievement.AchievementProgress
import com.example.wayspot.data.model.achievement.AchievementRules
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.review.ReviewFeedItem
import com.example.wayspot.data.model.user.UserProfile


/** Estado inmutable que reúne el perfil visible y sus reseñas. */
data class ProfileState(
    val userProfile: UserProfile? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val reviewFeedItems: List<ReviewFeedItem> = emptyList(),
    val areReviewsLoaded: Boolean = false,
    val expandedReviewIds: Set<String> = emptySet(),
    val areReviewLikesLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val achievements: List<AchievementProgress> =
        if (areReviewsLoaded) AchievementRules.calculate(reviews) else emptyList()
    val reviewLikeCounts: Map<String, Int> = reviews.associate { it.id to it.likesCount }
    val likedReviewIds: Set<String> = reviews.filter { it.liked }.map { it.id }.toSet()
}
