package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.ReviewRetrofitDataSourceImplementation
import com.example.wayspot.data.dto.toReviewInfo
import com.example.wayspot.data.model.ReviewInfo
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewRetrofitDataSourceImplementation
) {

    suspend fun getReviewsByPlaceId(
        placeId: String
    ): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsByPlaceId(placeId)

            val reviewsInfo = reviews.map {
                it.toReviewInfo()
            }

            Result.success(reviewsInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}