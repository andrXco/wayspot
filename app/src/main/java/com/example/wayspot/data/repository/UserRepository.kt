package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.UserRetrofitDataSourceImplementation
import com.example.wayspot.data.dto.toReviewInfo
import com.example.wayspot.data.dto.toBackendUser
import com.example.wayspot.data.model.BackendUser
import com.example.wayspot.data.model.ReviewInfo
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImplementation
) {

    suspend fun getUsers(): Result<List<BackendUser>> = runCatching {
        userRemoteDataSource.getUsers().map { it.toBackendUser() }
    }

    suspend fun getUserById(id: String): Result<BackendUser> = runCatching {
        userRemoteDataSource.getUserById(id).toBackendUser()
    }

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
