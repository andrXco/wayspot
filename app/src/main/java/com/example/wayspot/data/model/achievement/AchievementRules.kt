package com.example.wayspot.data.model.achievement

import com.example.wayspot.data.model.review.ReviewInfo

enum class AchievementType {
    FIRST_REVIEW,
    ACTIVE_EXPLORER,
    PHOTOGRAPHER,
    INFLUENCER,
    ADVENTURER
}

data class AchievementProgress(
    val type: AchievementType,
    val current: Int,
    val target: Int
) {
    val isUnlocked: Boolean
        get() = current >= target

    val fraction: Float
        get() = (current.toFloat() / target).coerceIn(0f, 1f)
}

/** Las insignias se calculan a partir de reseñas existentes; no requieren nuevos documentos. */
object AchievementRules {
    fun calculate(reviews: List<ReviewInfo>): List<AchievementProgress> {
        val totalReviews = reviews.size
        val reviewsWithPhotos = reviews.count { it.photoUrls.isNotEmpty() }
        val likesReceived = reviews.sumOf { it.likesCount.coerceAtLeast(0) }
        val placesReviewed = reviews.map { it.placeId }
            .filter { it.isNotBlank() }
            .distinct()
            .size

        return listOf(
            AchievementProgress(AchievementType.FIRST_REVIEW, totalReviews, 1),
            AchievementProgress(AchievementType.ACTIVE_EXPLORER, totalReviews, 5),
            AchievementProgress(AchievementType.PHOTOGRAPHER, reviewsWithPhotos, 3),
            AchievementProgress(AchievementType.INFLUENCER, likesReceived, 10),
            AchievementProgress(AchievementType.ADVENTURER, placesReviewed, 3)
        )
    }
}
