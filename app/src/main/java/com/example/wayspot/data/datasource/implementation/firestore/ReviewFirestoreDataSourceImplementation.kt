package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.dto.SaveReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import com.example.wayspot.data.dto.CommentFirestoreDto
import com.example.wayspot.data.dto.CreateCommentFirestoreDto
import com.example.wayspot.data.datasource.ReviewRemoteDataSource
import com.example.wayspot.data.dto.CreateCommentDto
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

    override suspend fun getReviewById(id: String): ReviewFirestoreDto {

        val docRef = db.collection("reviews").document(id)

        val snapshot = docRef.get().await()

        val review = snapshot.toObject(ReviewFirestoreDto::class.java)
            ?: throw Exception("No se pudo obtener la reseña")

        return review.copy(id = snapshot.id)
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

        docRef.set(updatedReview).await()

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

    suspend fun getReviewsByUserId(
        userId: String
    ): List<ReviewFirestoreDto> {

        val snapshot = db.collection("reviews")
            .whereEqualTo("userId", userId)
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
