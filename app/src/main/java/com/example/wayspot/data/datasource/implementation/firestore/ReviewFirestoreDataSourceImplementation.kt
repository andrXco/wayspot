package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import com.example.wayspot.data.dto.CommentFirestoreDto
import com.example.wayspot.data.dto.CreateCommentFirestoreDto
import com.example.wayspot.data.datasource.ReviewRemoteDataSource
import com.example.wayspot.data.dto.CreateCommentDto
import com.example.wayspot.data.dto.NotificationActorEventDto
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImplementation @Inject constructor(
    private val db: FirebaseFirestore
) : ReviewRemoteDataSource {

    override suspend fun createReview(
        review: SaveReviewDto
    ): ReviewFirestoreDto {

        val docRef = db.collection("reviews").add(review).await()

        return ReviewFirestoreDto(
            id = docRef.id,
            rating = review.rating,
            title = review.title,
            description = review.description,
            userId = review.userId,
            placeId = review.placeId,
            user = review.user
        )
    }

    override suspend fun getReviewsByPlaceId(
        placeId: String
    ): List<ReviewFirestoreDto> {

        val snapshot = db.collection("reviews")
            .whereEqualTo("placeId", placeId)
            .get()
            .await()

        val reviews = mutableListOf<ReviewFirestoreDto>()

        for (document in snapshot.documents) {
            val review = document.toObject(
                ReviewFirestoreDto::class.java
            )

            if (review != null) {
                reviews.add(review.copy(id = document.id))
            }
        }

        return reviews
    }

    override suspend fun getReviewById(
        id: String,
        currentUserId: String?
    ): ReviewFirestoreDto {

        val docRef = db.collection("reviews").document(id)

        val snapshot = docRef.get().await()

        val review = snapshot.toObject(ReviewFirestoreDto::class.java)
            ?: throw Exception("No se pudo obtener la reseña")

        val liked = if (!currentUserId.isNullOrBlank()) {

            val likeSnapshot = docRef.collection("likes")
                .document(currentUserId)
                .get()
                .await()

            likeSnapshot.exists()

        } else {
            false
        }

        return review.copy(
            id = snapshot.id,
            liked = liked
        )
    }

    override suspend fun updateReview(
        id: String,
        review: SaveReviewDto
    ): ReviewFirestoreDto {

        val originalReview = getReviewById(id)

        val updatedReview = originalReview.copy(
            rating = review.rating,
            title = review.title,
            description = review.description
        )

        val docRef = db.collection("reviews").document(id)

        docRef.update(
            mapOf(
                "rating" to review.rating,
                "title" to review.title,
                "description" to review.description
            )
        ).await()

        return updatedReview
    }

    override suspend fun getReviews(): List<ReviewFirestoreDto> {

        val snapshot = db.collection("reviews")
            .get()
            .await()

        val reviews = mutableListOf<ReviewFirestoreDto>()

        for (document in snapshot.documents) {
            val review = document.toObject(
                ReviewFirestoreDto::class.java
            )

            if (review != null) {
                reviews.add(review.copy(id = document.id))
            }
        }

        return reviews
    }


    override suspend fun deleteReview(id: String) {

        val docRef = db.collection("reviews").document(id)

        val commentsSnapshot = db.collection("comments")
            .whereEqualTo("reviewId", id)
            .get()
            .await()

        for (comment in commentsSnapshot.documents) {
            comment.reference.delete().await()
        }

        val likesSnapshot = docRef.collection("likes").get().await()

        for (like in likesSnapshot.documents) {
            like.reference.delete().await()
        }

        docRef.delete().await()
    }

    override suspend fun sendOrDeleteReviewLike(
        reviewId: String,
        userId: String
    ) {
        val reviewRef = db.collection("reviews")
            .document(reviewId)

        val likeRef = reviewRef.collection("likes")
            .document(userId)

        db.runTransaction { transaction ->

            val likeDoc = transaction.get(likeRef)

            if (likeDoc.exists()) {
                transaction.delete(likeRef)

                transaction.update(
                    reviewRef,
                    "likesCount",
                    FieldValue.increment(-1L)
                )

            } else {
                transaction.set(
                    likeRef,
                    mapOf(
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                )

                transaction.update(
                    reviewRef,
                    "likesCount",
                    FieldValue.increment(1L)
                )
            }

        }.await()
    }

    override suspend fun isReviewLiked(
        reviewId: String,
        userId: String
    ): Boolean = db.collection("reviews")
        .document(reviewId)
        .collection("likes")
        .document(userId)
        .get()
        .await()
        .exists()

    override suspend fun getReviewLikeEvents(reviewId: String): List<NotificationActorEventDto> =
        db.collection("reviews")
            .document(reviewId)
            .collection("likes")
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.getTimestamp("timestamp")?.toDate()?.time?.let { timestamp ->
                    NotificationActorEventDto(
                        actorId = document.id,
                        timestampMillis = timestamp
                    )
                }
            }

    override suspend fun createComment(
        reviewId: String,
        comment: CreateCommentDto
    ): CommentFirestoreDto {

        val firestoreComment = CreateCommentFirestoreDto(
            content = comment.content,
            reviewId = reviewId,
            userId = comment.userId,
            user = comment.user
        )

        val docRef = db.collection("comments")
            .add(firestoreComment)
            .await()

        return CommentFirestoreDto(
            id = docRef.id,
            content = comment.content,
            reviewId = reviewId,
            userId = comment.userId,
            user = comment.user
        )
    }

    override suspend fun getComments(
        reviewId: String
    ): List<CommentFirestoreDto> {

        val snapshot = db.collection("comments")
            .whereEqualTo("reviewId", reviewId)
            .get()
            .await()

        return snapshot.documents.map { document ->

            val comment = document.toObject(
                CommentFirestoreDto::class.java
            ) ?: throw Exception("No se pudo obtener el comentario")

            comment.copy(id = document.id)
        }
    }

    fun listenAllReviews(): Flow<List<ReviewFirestoreDto>> = callbackFlow {
        val listenerRegistration = db.collection("reviews")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshots == null) {
                    close(IllegalStateException("No se recibió una instantánea de reseñas"))
                    return@addSnapshotListener
                }

                val reviews = snapshots.documents.mapNotNull { document ->
                    document.toObject(ReviewFirestoreDto::class.java)
                        ?.copy(id = document.id)
                }

                trySend(reviews)
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}
