
package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.ReviewDtoGeneric
import com.example.wayspot.data.dto.SaveReviewDto
import com.example.wayspot.data.dto.CommentDtoGeneric
import com.example.wayspot.data.dto.CreateCommentDto


interface ReviewRemoteDataSource {

    suspend fun getReviews(): List<ReviewDtoGeneric>

    suspend fun getReviewById(id: String): ReviewDtoGeneric

    suspend fun getReviewsByPlaceId(
        placeId: String
    ): List<ReviewDtoGeneric>


    suspend fun createReview(review: SaveReviewDto): ReviewDtoGeneric
    suspend fun updateReview(id: String, review: SaveReviewDto): ReviewDtoGeneric
    suspend fun deleteReview(id: String)

    suspend fun getComments(
        reviewId: String
    ): List<CommentDtoGeneric>

    suspend fun createComment(
        reviewId: String,
        comment: CreateCommentDto
    ): CommentDtoGeneric

}
