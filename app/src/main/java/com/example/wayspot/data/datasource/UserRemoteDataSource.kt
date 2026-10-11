package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.UserDto

interface UserRemoteDataSource {

    suspend fun getUsers(): List<UserDto>
    suspend fun getUserById(id: String): UserDto

    suspend fun getReviewsByUserId(
        userId: String
    ): List<ReviewDto>
}
