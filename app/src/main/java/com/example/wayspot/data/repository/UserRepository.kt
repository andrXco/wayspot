package com.example.wayspot.data.repository

import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.datasource.implementation.firestore.UserFirestoreDataSourceImplementation
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userFirestoreDataSource: UserFirestoreDataSourceImplementation
) {

    suspend fun getUsers(): Result<List<UserInfo>> {
        return try {
            val users = userFirestoreDataSource.getUsers()
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
        userId: String,
        avatarUrl: String? = null
    ): Result<Unit> {
        return try {
            val userDto = RegisterUserDto(
                username = username,
                name = name,
                country = country,
                bio = bio,
                avatarUrl = avatarUrl
            )

            userFirestoreDataSource.updateUser(userDto, userId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserAvatar(
        userId: String,
        avatarUrl: String
    ): Result<Unit> {
        return try {
            userFirestoreDataSource.updateUserAvatar(
                userId,
                avatarUrl
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isFollowing(followerId: String, followedId: String): Result<Boolean> {
        return try {
            Result.success(userFirestoreDataSource.isFollowing(followerId, followedId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowersCount(userId: String): Result<Int> {
        return try {
            Result.success(userFirestoreDataSource.getFollowersCount(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowingCount(userId: String): Result<Int> {
        return try {
            Result.success(userFirestoreDataSource.getFollowingCount(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun followOrUnfollowUser(
        currentUserId: String,
        targetUserId: String
    ): Result<Unit> {
        return try {
            userFirestoreDataSource.followOrUnfollowUser(
                currentUserId,
                targetUserId
            )

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUserId(
        userId: String
    ): Result<List<ReviewInfo>> {
        return try {
            val reviews = userFirestoreDataSource.getReviewsByUserId(
                userId
            )

            val reviewsInfo = reviews.map {
                it.toReviewInfo()
            }

            Result.success(reviewsInfo)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
