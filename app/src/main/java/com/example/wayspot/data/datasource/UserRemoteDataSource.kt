package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.ReviewDto

interface UserRemoteDataSource {

    suspend fun getReviewsByUserId(
        userId: String
    ): List<ReviewDto>
}