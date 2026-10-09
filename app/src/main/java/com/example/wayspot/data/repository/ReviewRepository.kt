package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.ReviewRetrofitDataSourceImplementation
import com.example.wayspot.data.dto.BackendSession
import com.example.wayspot.data.dto.CreateCommentDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.example.wayspot.data.dto.toReviewComment
import com.example.wayspot.data.dto.toReviewInfo
import com.example.wayspot.data.model.ReviewComment
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.data.model.ReviewInfo
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewRetrofitDataSourceImplementation
) {

    suspend fun getReviews(): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviews()
            val reviewsInfo = reviews.map { it.toReviewInfo() }
            Result.success(reviewsInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewById(id: String): Result<ReviewInfo> {
        return try {
            val review = reviewRemoteDataSource.getReviewById(id)
            Result.success(review.toReviewInfo())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveReview(draft: ReviewDraft, reviewId: String? = null): Result<ReviewInfo> {
        return try {
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
            Result.success(saved.toReviewInfo())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(id: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.deleteReview(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getComments(reviewId: String): Result<List<ReviewComment>> {
        return try {
            val comments = reviewRemoteDataSource.getComments(reviewId)
            val commentsInfo = comments.map { it.toReviewComment() }
            Result.success(commentsInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createComment(reviewId: String, content: String): Result<ReviewComment> {
        return try {
            val comment = reviewRemoteDataSource.createComment(
                reviewId,
                CreateCommentDto(content, BackendSession.USER_ID.toInt())
            )
            Result.success(comment.toReviewComment())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByPlaceId(placeId: String): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsByPlaceId(placeId)
            val reviewsInfo = reviews.map { it.toReviewInfo() }
            Result.success(reviewsInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
