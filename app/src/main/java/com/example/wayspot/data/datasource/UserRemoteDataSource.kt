
package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.UserDtoGeneric
import com.example.wayspot.data.dto.ReviewDtoGeneric
import com.example.wayspot.data.model.user.UserRelationshipListType
import com.example.wayspot.data.dto.NotificationActorEventDto

interface UserRemoteDataSource {

    suspend fun getUsers(): List<UserDtoGeneric>

    suspend fun getUsersByIds(userIds: List<String>): List<UserDtoGeneric>

    suspend fun getUserById(id: String): UserDtoGeneric?

    suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    )

    suspend fun updateUser(
        userDto: RegisterUserDto,
        userId: String
    )

    suspend fun updateUserAvatar(
        userId: String,
        avatarUrl: String
    )

    suspend fun isFollowing(
        followerId: String,
        followedId: String
    ): Boolean

    suspend fun getFollowersCount(userId: String): Int

    suspend fun getFollowingCount(userId: String): Int

    suspend fun getUserConnections(
        userId: String,
        relationshipType: UserRelationshipListType
    ): List<UserDtoGeneric>

    suspend fun getUserConnectionIds(
        userId: String,
        relationshipType: UserRelationshipListType
    ): List<String>

    suspend fun getNotificationActorEvents(
        userId: String,
        relationshipType: UserRelationshipListType
    ): List<NotificationActorEventDto>

    suspend fun getReviewsByUserId(
        userId: String
    ): List<ReviewDtoGeneric>

    suspend fun followOrUnfollowUser(
        currentUserId: String,
        targetUserId: String
    )

}
