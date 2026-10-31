package com.example.wayspot.data.repository

import com.example.wayspot.data.model.review.ReviewComment
import com.example.wayspot.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImplementation
import com.example.wayspot.data.model.review.ReviewDraft
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.notification.NotificationActorEvent
import com.example.wayspot.data.dto.CreateCommentDto
import com.example.wayspot.data.dto.ReviewUserDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.example.wayspot.data.dto.CommentFirestoreUserDto
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReviewRepository @Inject constructor(
    private val reviewFirestoreDataSource: ReviewFirestoreDataSourceImplementation,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend fun getReviewLikeNotificationEvents(
        reviewIds: List<String>
    ): Result<Map<String, List<NotificationActorEvent>>> = try {
        val events = reviewIds.distinct().associateWith { reviewId ->
            reviewFirestoreDataSource.getReviewLikeEvents(reviewId).map { event ->
                NotificationActorEvent(event.actorId, event.timestampMillis)
            }
        }
        Result.success(events)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getReviews(): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewFirestoreDataSource.getReviews()

            val reviewsInfo = reviews.map { it.toReviewInfo() }
            val authors = userRepository.getUsersByIds(reviewsInfo.map { it.userId })
                .getOrNull()
                .orEmpty()
                .associateBy { it.id }
            val reviewsWithCurrentAvatars = reviewsInfo.map { review ->
                val author = authors[review.userId]
                review.copy(
                    authorName = review.authorName?.takeIf(String::isNotBlank)
                        ?: author?.name,
                    authorUsername = review.authorUsername?.takeIf(String::isNotBlank)
                        ?: author?.username,
                    authorAvatarUrl = author?.avatarUrl?.takeIf(String::isNotBlank)
                        ?: review.authorAvatarUrl
                )
            }

            Result.success(reviewsWithCurrentAvatars)

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllReviews(): Flow<List<ReviewInfo>> =
        reviewFirestoreDataSource.listenAllReviews().map { reviews ->
            reviews.map { review -> review.toReviewInfo() }
        }

    suspend fun getReviewById(id: String): Result<ReviewInfo> {
        return try {
            val currentUserId = authRepository.currentUser?.uid

            val review = reviewFirestoreDataSource.getReviewById(
                id,
                currentUserId
            )

            Result.success(review.toReviewInfo())

        } catch (e: CancellationException) {
            throw e
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
                    username = user.username,
                    avatarUrl = user.avatarUrl
                )

            )


            val savedReview = reviewFirestoreDataSource.createReview(
                reviewDto
            )

            Result.success(savedReview.toReviewInfo())

        } catch (e: CancellationException) {
            throw e
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

        } catch (e: CancellationException) {
            throw e
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

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendOrDeleteReviewLike(
        reviewId: String
    ): Result<Unit> {
        return try {
            val userId = authRepository.currentUser?.uid
                ?: throw Exception("Usuario no autenticado")

            reviewFirestoreDataSource.sendOrDeleteReviewLike(
                reviewId,
                userId
            )

            Result.success(Unit)

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLikedReviewIds(reviewIds: List<String>): Result<Set<String>> {
        val userId = authRepository.currentUser?.uid
            ?: return Result.success(emptySet())

        return try {
            val likedReviews = reviewIds.distinct().chunked(LIKE_LOOKUP_BATCH_SIZE)
                .flatMap { batch ->
                    coroutineScope {
                        batch.map { reviewId ->
                            async {
                                reviewId to reviewFirestoreDataSource.isReviewLiked(reviewId, userId)
                            }
                        }.awaitAll()
                    }
                }

            Result.success(
                likedReviews.filter { (_, liked) -> liked }
                    .map { (reviewId, _) -> reviewId }
                    .toSet()
            )
        } catch (e: CancellationException) {
            throw e
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

            val commentsInfo = comments.map { commentDto ->
                commentDto.toReviewComment()
            }

            val needsUserFallback = commentsInfo.any { comment ->
                comment.author.name.isBlank() ||
                        comment.author.username.isBlank() ||
                        comment.author.avatarUrl.isNullOrBlank()
            }

            val users = if (needsUserFallback) {
                userRepository.getUsers()
                    .getOrNull()
                    .orEmpty()
            } else {
                emptyList()
            }

            val updatedComments = commentsInfo.map { comment ->
                val user = users.find {
                    it.id == comment.author.id
                }

                if (user != null) {
                    comment.copy(
                        author = comment.author.copy(
                            name = if (comment.author.name.isNotBlank()) {
                                comment.author.name
                            } else {
                                user.name
                            },
                            username = if (comment.author.username.isNotBlank()) {
                                comment.author.username
                            } else {
                                user.username
                            },
                            avatarUrl = if (!comment.author.avatarUrl.isNullOrBlank()) {
                                comment.author.avatarUrl
                            } else {
                                user.avatarUrl
                            }
                        )
                    )
                } else {
                    comment
                }
            }

            Result.success(updatedComments)

        } catch (e: CancellationException) {
            throw e
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
                    name = user.name,
                    username = user.username,
                    avatarUrl = user.avatarUrl
                )
            )


            val savedComment = reviewFirestoreDataSource.createComment(
                reviewId,
                commentDto
            )

            Result.success(savedComment.toReviewComment())

        } catch (e: CancellationException) {
            throw e
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

            val reviewsInfo = reviews.map { it.toReviewInfo() }
            val authors = userRepository.getUsersByIds(reviewsInfo.map { it.userId })
                .getOrNull()
                .orEmpty()
                .associateBy { it.id }
            Result.success(reviewsInfo.map { review ->
                val author = authors[review.userId]
                review.copy(
                    authorName = review.authorName?.takeIf(String::isNotBlank) ?: author?.name,
                    authorUsername = review.authorUsername?.takeIf(String::isNotBlank) ?: author?.username,
                    authorAvatarUrl = author?.avatarUrl?.takeIf(String::isNotBlank)
                        ?: review.authorAvatarUrl
                )
            })

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private companion object {
        const val LIKE_LOOKUP_BATCH_SIZE = 10
    }

}
