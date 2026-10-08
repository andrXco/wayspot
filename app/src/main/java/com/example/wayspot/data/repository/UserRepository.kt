package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.UserRetrofitDataSourceImplementation
import com.example.wayspot.data.dto.UserDto
import com.example.wayspot.data.dto.toReviewInfo
import com.example.wayspot.data.model.ReviewInfo
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImplementation
) {

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = userRemoteDataSource.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(id: String): Result<UserDto> {
        return try {
            val user = userRemoteDataSource.getUserById(id)
            Result.success(user)
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
