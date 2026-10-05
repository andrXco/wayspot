package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.UserRetrofitDataSourceImplementation
import com.example.wayspot.data.dto.toReviewInfo
import com.example.wayspot.data.model.ReviewInfo
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImplementation
) {

    suspend fun getReviewsByUserId(
        userId: String
    ): Result<List<ReviewInfo>> {
        return try {
            val reviews = userRemoteDataSource.getReviewsByUserId(userId)

            val reviewsInfo = reviews.map {
                it.toReviewInfo()
            }

            Result.success(reviewsInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}