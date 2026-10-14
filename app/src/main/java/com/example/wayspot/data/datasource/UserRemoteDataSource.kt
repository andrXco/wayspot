
package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.UserDto
import com.example.wayspot.data.dto.UserDtoGeneric

interface UserRemoteDataSource {

    suspend fun getUsers(): List<UserDto>

    suspend fun getUserById(id: String): UserDtoGeneric

    suspend fun getReviewsByUserId(
        userId: String
    ): List<ReviewDto>

    suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    )
}
