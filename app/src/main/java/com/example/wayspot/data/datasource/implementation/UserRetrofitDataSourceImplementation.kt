
package com.example.wayspot.data.datasource.implementation

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.datasource.services.UserRetrofitService
import com.example.wayspot.data.dto.RegisterUserDto
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
        TODO("Not yet implemented")
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
    }


}

