
package com.example.wayspot.data.datasource.implementation

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.datasource.services.UserRetrofitService
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.UserConnectionsPageDto
import com.example.wayspot.data.model.user.UserRelationshipListType
import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.UserDto
import javax.inject.Inject

class UserRetrofitDataSourceImplementation @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getUsers(): List<UserDto> = service.getUsers()

    override suspend fun getUserById(id: String): UserDto =
        service.getUserById(id)

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        throw UnsupportedOperationException("User registration is not available through the REST data source.")
    }

    override suspend fun updateUser(
        userDto: RegisterUserDto,
        userId: String
    ) {
        throw UnsupportedOperationException("User updates are not available through the REST data source.")
    }

    override suspend fun updateUserAvatar(
        userId: String,
        avatarUrl: String
    ) {
        throw UnsupportedOperationException("Avatar updates are not available through the REST data source.")
    }

    override suspend fun isFollowing(
        followerId: String,
        followedId: String
    ): Boolean {
        throw UnsupportedOperationException("Follow state is not available through the REST data source.")
    }

    override suspend fun getFollowersCount(userId: String): Int {
        throw UnsupportedOperationException("Follower counts are not available through the REST data source.")
    }

    override suspend fun getFollowingCount(userId: String): Int {
        throw UnsupportedOperationException("Following counts are not available through the REST data source.")
    }

    override suspend fun getUserConnections(
        userId: String,
        relationshipType: UserRelationshipListType,
        cursor: String?,
        pageSize: Long
    ): UserConnectionsPageDto {
        throw UnsupportedOperationException("User connections are not available through the REST data source.")
    }

    override suspend fun getReviewsByUserId(
        userId: String
    ): List<ReviewDto> {
        return service.getReviewsByUserId(userId)
    }

    override suspend fun followOrUnfollowUser(
        currentUserId: String,
        targetUserId: String
    ) {
        throw UnsupportedOperationException("Follow operations are not available through the REST data source.")
    }


}

