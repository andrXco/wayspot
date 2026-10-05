package com.example.wayspot.data.datasource.services

import com.example.wayspot.data.dto.ReviewDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRetrofitService {

    @GET("/users/{userId}/reviews")
    suspend fun getReviewsByUserId(
        @Path("userId") userId: String
    ): List<ReviewDto>
}