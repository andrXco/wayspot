package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.ReviewDto

interface ReviewRemoteDataSource {

    suspend fun getReviewsByPlaceId(
        placeId: String
    ): List<ReviewDto>
}