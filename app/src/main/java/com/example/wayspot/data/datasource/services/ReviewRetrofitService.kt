package com.example.wayspot.data.datasource.services

import com.example.wayspot.data.dto.ReviewDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewRetrofitService {

    @GET("/places/{placeId}/reviews")
    suspend fun getReviewsByPlaceId(
        @Path("placeId") placeId: String
    ): List<ReviewDto>
}