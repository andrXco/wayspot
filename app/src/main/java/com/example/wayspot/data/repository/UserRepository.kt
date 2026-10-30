package com.example.wayspot.data.repository

import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.model.user.ProfileNotificationPreferences
import com.example.wayspot.data.model.user.UserRelationshipListType
import com.example.wayspot.data.model.user.UserRelationshipRules
import com.example.wayspot.data.model.notification.NotificationActorEvent
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
            if (user == null) {
                return Result.failure(NoSuchElementException("Usuario $id no encontrado"))
            }
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
            val fcmToken = getFcmTokenOrEmpty()
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
            val fcmToken = getFcmTokenOrEmpty()
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

    private suspend fun getFcmTokenOrEmpty(): String = try {
        FirebaseMessaging.getInstance().token.await()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        ""
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
        relationshipType: UserRelationshipListType
    ): Result<List<UserInfo>> {
        return try {
            val users = userRemoteDataSource.getUserConnections(
                userId = userId,
                relationshipType = relationshipType
            )
            Result.success(users.map { it.toUserInfo() })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowerNotificationEvents(userId: String): Result<List<NotificationActorEvent>> = try {
        Result.success(
            userRemoteDataSource.getNotificationActorEvents(
                userId = userId,
                relationshipType = UserRelationshipListType.FOLLOWERS
            ).map { NotificationActorEvent(it.actorId, it.timestampMillis) }
        )
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUsersByIds(userIds: List<String>): Result<List<UserInfo>> = try {
        val users = userIds.distinct()
            .chunked(UserRelationshipRules.MAX_USER_IDS_PER_QUERY)
            .flatMap { ids -> userRemoteDataSource.getUsersByIds(ids) }
        Result.success(users.map { it.toUserInfo() })
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getFollowingUserIds(userId: String): Result<Set<String>> {
        return try {
            val followingUserIds = userRemoteDataSource.getUserConnectionIds(
                userId = userId,
                relationshipType = UserRelationshipListType.FOLLOWING
            )
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
