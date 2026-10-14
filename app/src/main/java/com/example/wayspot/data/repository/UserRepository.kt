package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.UserRetrofitDataSourceImplementation
import com.example.wayspot.data.mapper.toReviewInfo
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.datasource.implementation.firestore.UserFirestoreDataSourceImplementation
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImplementation,
    private val userFirestoreDataSource: UserFirestoreDataSourceImplementation
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
            val user = userFirestoreDataSource.getUserById(id)
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

    suspend fun registerUser(
        username: String,
        name: String,
        country: String?,
        bio: String?,
        userId: String
    ): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(
                username = username,
                name = name,
                country = country,
                bio = bio
            )

            userFirestoreDataSource.registerUser(
                registerUserDto,
                userId
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(
        name: String,
        username: String,
        country: String?,
        bio: String?,
        userId: String
    ): Result<Unit> {
        return try {
            val userDto = RegisterUserDto(
                username = username,
                name = name,
                country = country,
                bio = bio
            )

            userFirestoreDataSource.updateUser(userDto, userId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
