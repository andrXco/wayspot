
package com.example.wayspot.data.dto

import com.example.wayspot.data.model.review.ReviewInfo

data class ReviewFirestoreDto(
    val id: String = "",
    val rating: Int = 0,
    val title: String? = null,
    val description: String = "",
    val userId: String = "",
    val placeId: String = "",
    val reviewDate: String? = null,
    val authorName: String? = null,
    val user: ReviewUserDto? = null,
    val likesCount: Int = 0,
    val liked: Boolean = false
) : ReviewDtoGeneric() {

    override fun toReviewInfo(): ReviewInfo {
        return ReviewInfo(
            id = id,
            rating = rating,
            title = title,
            description = description,
            userId = userId,
            placeId = placeId,
            reviewDate = reviewDate,
            authorName = user?.name ?: authorName,
            authorUsername = user?.username,
            authorAvatarUrl = user?.avatarUrl,
            likesCount = likesCount,
            liked = liked
        )
    }

}
