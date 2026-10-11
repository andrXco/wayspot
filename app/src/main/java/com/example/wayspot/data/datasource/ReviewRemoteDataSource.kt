package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.example.wayspot.data.dto.CommentDto
import com.example.wayspot.data.dto.CreateCommentDto

interface ReviewRemoteDataSource {

    suspend fun getReviews(): List<ReviewDto>
    suspend fun getReviewById(id: String): ReviewDto

    suspend fun getReviewsByPlaceId(
        placeId: String
    ): List<ReviewDto>

    suspend fun createReview(review: SaveReviewDto): ReviewDto
    suspend fun updateReview(id: String, review: SaveReviewDto): ReviewDto
    suspend fun deleteReview(id: String)
    suspend fun getComments(reviewId: String): List<CommentDto>
    suspend fun createComment(reviewId: String, comment: CreateCommentDto): CommentDto
}
