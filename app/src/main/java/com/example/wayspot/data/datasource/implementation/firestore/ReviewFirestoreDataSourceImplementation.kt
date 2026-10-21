package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import com.example.wayspot.data.dto.CommentFirestoreDto
import com.example.wayspot.data.dto.CreateCommentFirestoreDto
import com.example.wayspot.data.datasource.ReviewRemoteDataSource
import com.example.wayspot.data.dto.CreateCommentDto
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImplementation @Inject constructor(
    private val db: FirebaseFirestore
) : ReviewRemoteDataSource {


    override suspend fun createReview(
        review: SaveReviewDto
    ): ReviewFirestoreDto {

        val docRef = db.collection("reviews")
            .add(review)
            .await()

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

        android.util.Log.d(
            "WaySpotLikes",
            "Review: $id | Usuario: $currentUserId | Liked: $liked | Contador: ${review.likesCount}"
        )


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

        val commentsSnapshot = db.collection("comments")
            .whereEqualTo("reviewId", id)
            .get()
            .await()

        for (document in commentsSnapshot.documents) {
            document.reference.delete().await()
        }

        val docRef = db.collection("reviews").document(id)

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

}
