package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.ReviewRetrofitDataSourceImplementation
import com.example.wayspot.data.dto.BackendSession
import com.example.wayspot.data.dto.CreateCommentDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.example.wayspot.data.mapper.toReviewComment
import com.example.wayspot.data.mapper.toReviewInfo
import com.example.wayspot.data.model.review.ReviewComment
import com.example.wayspot.data.model.review.ReviewDraft
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImplementation
import com.example.wayspot.data.dto.ReviewFirestoreDto

import javax.inject.Inject


class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewRetrofitDataSourceImplementation,
    private val reviewFirestoreDataSource: ReviewFirestoreDataSourceImplementation,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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
            val review = reviewFirestoreDataSource.getReviewById(id)

            Result.success(review.toReviewInfo())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun createReviewInFirestore(
        draft: ReviewDraft
    ): Result<ReviewInfo> {
        return try {
            val userId = authRepository.currentUser?.uid
                ?: throw Exception("Usuario no autenticado")

            val userResult = userRepository.getUserById(userId)

            val user = userResult.getOrNull()
                ?: throw Exception("No se pudo obtener el perfil del usuario")

            val reviewDto = ReviewFirestoreDto(
                rating = draft.rating,
                title = draft.title,
                description = draft.description,
                userId = userId,
                placeId = draft.placeId,
                authorName = user.name
            )

            val savedReview = reviewFirestoreDataSource.createReview(
                reviewDto
            )

            Result.success(savedReview.toReviewInfo())

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun updateReviewInFirestore(
        draft: ReviewDraft,
        reviewId: String
    ): Result<ReviewInfo> {
        return try {
            val userId = authRepository.currentUser?.uid
                ?: throw Exception("Usuario no autenticado")

            val originalReview = reviewFirestoreDataSource.getReviewById(
                reviewId
            )

            if (originalReview.userId != userId) {
                throw Exception("No puedes editar una reseña de otro usuario")
            }

            val updatedReview = originalReview.copy(
                rating = draft.rating,
                title = draft.title,
                description = draft.description
            )

            reviewFirestoreDataSource.updateReview(updatedReview)

            Result.success(updatedReview.toReviewInfo())

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
            val userId = authRepository.currentUser?.uid
                ?: throw Exception("Usuario no autenticado")

            val review = reviewFirestoreDataSource.getReviewById(id)

            if (review.userId != userId) {
                throw Exception("No puedes eliminar una reseña de otro usuario")
            }

            reviewFirestoreDataSource.deleteReview(id)

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

    suspend fun getReviewsByPlaceId(
        placeId: String
    ): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewFirestoreDataSource.getReviewsByPlaceId(
                placeId
            )

            val reviewsInfo = reviews.map {
                it.toReviewInfo()
            }

            Result.success(reviewsInfo)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUserId(
        userId: String
    ): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewFirestoreDataSource.getReviewsByUserId(
                userId
            )

            val reviewsInfo = reviews.map {
                it.toReviewInfo()
            }

            Result.success(reviewsInfo)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
