package com.example.wayspot.data.datasource.implementation

import com.example.wayspot.data.datasource.ReviewRemoteDataSource
import com.example.wayspot.data.datasource.services.ReviewRetrofitService
import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.example.wayspot.data.dto.CommentDto
import com.example.wayspot.data.dto.CreateCommentDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImplementation @Inject constructor(
    private val service: ReviewRetrofitService
) : ReviewRemoteDataSource {

    override suspend fun getReviews() = service.getReviews()
    override suspend fun getReviewById(id: String) = service.getReviewById(id)

    override suspend fun getReviewsByPlaceId(
        placeId: String
    ): List<ReviewDto> {
        return service.getReviewsByPlaceId(placeId)
    }

    override suspend fun createReview(review: SaveReviewDto) = service.createReview(review)
    override suspend fun updateReview(id: String, review: SaveReviewDto) = service.updateReview(id, review)
    override suspend fun deleteReview(id: String) = service.deleteReview(id)
    override suspend fun getComments(reviewId: String): List<CommentDto> = service.getComments(reviewId)
    override suspend fun createComment(reviewId: String, comment: CreateCommentDto) =
        service.createComment(reviewId, comment)
}
