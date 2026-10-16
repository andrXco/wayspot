
package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.UserDtoGeneric
import com.example.wayspot.data.dto.ReviewDtoGeneric

interface UserRemoteDataSource {

    suspend fun getUsers(): List<UserDtoGeneric>

    suspend fun getUserById(id: String): UserDtoGeneric

    suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    )
    suspend fun getReviewsByUserId(
        userId: String
    ): List<ReviewDtoGeneric>

}
