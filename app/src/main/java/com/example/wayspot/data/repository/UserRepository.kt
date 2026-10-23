package com.example.wayspot.data.repository

import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.UserConnectionsPageDto
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.model.user.ProfileNotificationPreferences
import com.example.wayspot.data.model.user.UserConnectionsPage
import com.example.wayspot.data.model.user.UserRelationshipListType
import com.example.wayspot.data.model.user.UserRelationshipRules
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) {

    suspend fun getUsers(): Result<List<UserInfo>> {
        return try {
            val users = userRemoteDataSource.getUsers()
            Result.success(users.map { it.toUserInfo() })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(id: String): Result<UserInfo> {
        return try {
            val user = userRemoteDataSource.getUserById(id)
            Result.success(user.toUserInfo())
        } catch (e: CancellationException) {
            throw e
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
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            val registerUserDto = RegisterUserDto(
                username = username,
                name = name,
                fcmToken = fcmToken,
                country = country,
                bio = bio,
                notificationPreferences = ProfileNotificationPreferences()
            )

            userRemoteDataSource.registerUser(
                registerUserDto,
                userId
            )

            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
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
        avatarUrl: String? = null,
        notificationPreferences: ProfileNotificationPreferences =
            ProfileNotificationPreferences()
    ): Result<Unit> {
        return try {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            val userDto = RegisterUserDto(
                username = username,
                name = name,
                fcmToken = fcmToken,
                country = country,
                bio = bio,
                avatarUrl = avatarUrl,
                notificationPreferences = notificationPreferences
            )

            userRemoteDataSource.updateUser(userDto, userId)

            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserAvatar(
        userId: String,
        avatarUrl: String
    ): Result<Unit> {
        return try {
            userRemoteDataSource.updateUserAvatar(
                userId,
                avatarUrl
            )

            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isFollowing(followerId: String, followedId: String): Result<Boolean> {
        return try {
            Result.success(userRemoteDataSource.isFollowing(followerId, followedId))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowersCount(userId: String): Result<Int> {
        return try {
            Result.success(userRemoteDataSource.getFollowersCount(userId))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowingCount(userId: String): Result<Int> {
        return try {
            Result.success(userRemoteDataSource.getFollowingCount(userId))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserConnections(
        userId: String,
        relationshipType: UserRelationshipListType,
        cursor: String? = null
    ): Result<UserConnectionsPage> {
        return try {
            val page: UserConnectionsPageDto = userRemoteDataSource.getUserConnections(
                userId = userId,
                relationshipType = relationshipType,
                cursor = cursor,
                pageSize = UserRelationshipRules.PAGE_SIZE
            )
            Result.success(
                UserConnectionsPage(
                    users = page.users.map { it.toUserInfo() },
                    nextCursor = page.nextCursor
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowingUserIds(userId: String): Result<Set<String>> {
        return try {
            val followingUserIds = linkedSetOf<String>()
            var cursor: String? = null

            do {
                val page = userRemoteDataSource.getUserConnections(
                    userId = userId,
                    relationshipType = UserRelationshipListType.FOLLOWING,
                    cursor = cursor,
                    pageSize = UserRelationshipRules.PAGE_SIZE
                )
                followingUserIds.addAll(page.users.map { user -> user.toUserInfo().id })
                cursor = page.nextCursor
            } while (cursor != null)

            Result.success(followingUserIds.toSet())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun followOrUnfollowUser(
        currentUserId: String,
        targetUserId: String
    ): Result<Unit> {
        return try {
            userRemoteDataSource.followOrUnfollowUser(
                currentUserId,
                targetUserId
            )

            Result.success(Unit)

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUserId(
        userId: String
    ): Result<List<ReviewInfo>> {
        return try {
            val reviews = userRemoteDataSource.getReviewsByUserId(
                userId
            )

            val reviewsInfo = reviews.map {
                it.toReviewInfo()
            }

            Result.success(reviewsInfo)

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
