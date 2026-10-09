package com.example.wayspot.data.datasource.services

import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRetrofitService {

    @GET("/users")
    suspend fun getUsers(): List<UserDto>

    @GET("/users/{id}")
    suspend fun getUserById(@Path("id") id: String): UserDto

    @GET("/users/{userId}/reviews")
    suspend fun getReviewsByUserId(
        @Path("userId") userId: String
    ): List<ReviewDto>
}
