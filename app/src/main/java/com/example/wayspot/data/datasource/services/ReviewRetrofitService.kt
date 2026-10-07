package com.example.wayspot.data.datasource.services

import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.example.wayspot.data.dto.CommentDto
import com.example.wayspot.data.dto.CreateCommentDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.PUT

interface ReviewRetrofitService {

    @GET("/reviews")
    suspend fun getReviews(): List<ReviewDto>

    @GET("/reviews/{id}")
    suspend fun getReviewById(@Path("id") id: String): ReviewDto

    @GET("/places/{placeId}/reviews")
    suspend fun getReviewsByPlaceId(
        @Path("placeId") placeId: String
    ): List<ReviewDto>

    @POST("/reviews")
    suspend fun createReview(@Body review: SaveReviewDto): ReviewDto

    @PUT("/reviews/{id}")
    suspend fun updateReview(@Path("id") id: String, @Body review: SaveReviewDto): ReviewDto

    @DELETE("/reviews/{id}")
    suspend fun deleteReview(@Path("id") id: String): Unit

    @GET("/reviews/{reviewId}/comments")
    suspend fun getComments(@Path("reviewId") reviewId: String): List<CommentDto>

    @POST("/reviews/{reviewId}/comments")
    suspend fun createComment(
        @Path("reviewId") reviewId: String,
        @Body comment: CreateCommentDto
    ): CommentDto
}
