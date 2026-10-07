package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.ReviewRetrofitDataSourceImplementation
import com.example.wayspot.data.dto.toReviewInfo
import com.example.wayspot.data.dto.toReviewComment
import com.example.wayspot.data.dto.SaveReviewDto
import com.example.wayspot.data.dto.CreateCommentDto
import com.example.wayspot.data.model.BackendSession
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.data.model.ReviewFeedItem
import com.example.wayspot.data.model.ReviewComment
import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.data.model.ReviewInfo
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewRetrofitDataSourceImplementation,
    private val userRepository: UserRepository
) {

    suspend fun getReviews(): Result<List<ReviewInfo>> = runCatching {
        reviewRemoteDataSource.getReviews().map { it.toReviewInfo() }
    }

    suspend fun getReviewById(id: String): Result<ReviewInfo> = runCatching {
        reviewRemoteDataSource.getReviewById(id).toReviewInfo()
    }

    suspend fun getFeed(availablePlaces: List<PlaceInfo>): Result<List<ReviewFeedItem>> = runCatching {
        val places = availablePlaces.associateBy { it.id }
        val users = userRepository.getUsers().getOrThrow().associateBy { it.id }
        getReviews().getOrThrow().mapNotNull { review ->
            val place = places[review.placeId] ?: return@mapNotNull null
            val author = users[review.userId] ?: return@mapNotNull null
            ReviewFeedItem(review, place, author)
        }
    }

    suspend fun saveReview(draft: ReviewDraft, reviewId: String? = null): Result<ReviewInfo> = runCatching {
        val body = SaveReviewDto(
            rating = draft.rating,
            title = draft.title,
            description = draft.description,
            userId = BackendSession.USER_ID.toInt(),
            placeId = draft.placeId.toInt()
        )
        val saved = if (reviewId == null) {
            reviewRemoteDataSource.createReview(body)
        } else {
            reviewRemoteDataSource.updateReview(reviewId, body)
        }
        saved.toReviewInfo()
    }

    suspend fun deleteReview(id: String): Result<Unit> = runCatching {
        reviewRemoteDataSource.deleteReview(id)
    }

    suspend fun getComments(reviewId: String): Result<List<ReviewComment>> = runCatching {
        reviewRemoteDataSource.getComments(reviewId).map { it.toReviewComment() }
    }

    suspend fun createComment(reviewId: String, content: String): Result<ReviewComment> = runCatching {
        reviewRemoteDataSource.createComment(
            reviewId,
            CreateCommentDto(content.trim(), BackendSession.USER_ID.toInt())
        ).toReviewComment()
    }

    suspend fun getReviewsByPlaceId(
        placeId: String
    ): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsByPlaceId(placeId)

            val reviewsInfo = reviews.map {
                it.toReviewInfo()
            }

            Result.success(reviewsInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
