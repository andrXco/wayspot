package com.example.wayspot.data.repository

import com.example.wayspot.data.model.review.ReviewComment
import com.example.wayspot.data.model.review.ReviewDraft
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImplementation
import com.example.wayspot.data.dto.CreateCommentDto
import com.example.wayspot.data.dto.ReviewUserDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.example.wayspot.data.dto.CommentFirestoreUserDto
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewFirestoreDataSource: ReviewFirestoreDataSourceImplementation,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

/*
    suspend fun getReviews(): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewFirestoreDataSource.getReviews()
            val reviewsInfo = reviews.map { it.toReviewInfo() }
            Result.success(reviewsInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
*/


    suspend fun getReviews(): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewFirestoreDataSource.getReviews()

            val reviewsInfo = reviews.map {
                it.toReviewInfo()
            }

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

            val reviewDto = SaveReviewDto(
                rating = draft.rating,
                title = draft.title,
                description = draft.description,
                userId = userId,
                placeId = draft.placeId,
                user = ReviewUserDto(
                    name = user.name,
                    username = user.username
                )
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

            val reviewDto = SaveReviewDto(
                rating = draft.rating,
                title = draft.title,
                description = draft.description,
                userId = originalReview.userId,
                placeId = originalReview.placeId,
                user = originalReview.user
            )

            val updatedReview = reviewFirestoreDataSource.updateReview(
                reviewId,
                reviewDto
            )

            Result.success(updatedReview.toReviewInfo())

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

    suspend fun getComments(
        reviewId: String
    ): Result<List<ReviewComment>> {
        return try {
            val comments = reviewFirestoreDataSource.getComments(
                reviewId
            )

            val commentsInfo = comments.map {
                it.toReviewComment()
            }

            Result.success(commentsInfo)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createComment(
        reviewId: String,
        content: String
    ): Result<ReviewComment> {
        return try {
            val userId = authRepository.currentUser?.uid
                ?: throw Exception("Usuario no autenticado")

            val userResult = userRepository.getUserById(userId)

            val user = userResult.getOrNull()
                ?: throw Exception("No se pudo obtener el perfil del usuario")

            val commentDto = CreateCommentDto(
                content = content,
                userId = userId,
                user = CommentFirestoreUserDto(
                    name = user.name
                )
            )

            val savedComment = reviewFirestoreDataSource.createComment(
                reviewId,
                commentDto
            )

            Result.success(savedComment.toReviewComment())

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

}
