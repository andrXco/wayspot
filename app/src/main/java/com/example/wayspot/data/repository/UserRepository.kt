package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.UserRetrofitDataSourceImplementation
import com.example.wayspot.data.dto.toUserInfo
import com.example.wayspot.data.dto.toReviewInfo
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.user.UserInfo
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImplementation
) {

    suspend fun getUsers(): Result<List<UserInfo>> {
        return try {
            val users = userRemoteDataSource.getUsers()
            Result.success(users.map { it.toUserInfo() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(id: String): Result<UserInfo> {
        return try {
            val user = userRemoteDataSource.getUserById(id)
            Result.success(user.toUserInfo())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUserId(userId: String): Result<List<ReviewInfo>> {
        return try {
            val reviews = userRemoteDataSource.getReviewsByUserId(userId)
            val reviewsInfo = reviews.map { it.toReviewInfo() }
            val recentReviews = mutableListOf<ReviewInfo>()
            for (index in reviewsInfo.size - 1 downTo 0) {
                recentReviews.add(reviewsInfo[index])
            }
            Result.success(recentReviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
