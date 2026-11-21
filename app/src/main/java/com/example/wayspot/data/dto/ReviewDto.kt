
package com.example.wayspot.data.dto

import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.place.PlaceCoordinates

data class ReviewDto(
    val id: Int,
    val rating: Int,
    val title: String?,
    val description: String,
    val userId: Int,
    val placeId: Int?,
    val coordinates: PlaceCoordinates? = null,
    val reviewDate: String? = null,
    val user: ReviewUserDto? = null
) : ReviewDtoGeneric() {

    override fun toReviewInfo(): ReviewInfo {
        return ReviewInfo(
            id = id.toString(),
            rating = rating,
            title = title,
            description = description,
            userId = userId.toString(),
            placeId = placeId?.toString(),
            coordinates = coordinates,
            reviewDate = reviewDate,
            authorName = user?.name
        )
    }
}


data class ReviewUserDto(
    val name: String? = null,
    val username: String? = null,
    val avatarUrl: String? = null
)



data class SaveReviewDto(
    val rating: Int,
    val title: String,
    val description: String,
    val userId: String,
    val placeId: String?,
    val coordinates: PlaceCoordinates? = null,
    val user: ReviewUserDto? = null,
    val likesCount: Int = 0
)
