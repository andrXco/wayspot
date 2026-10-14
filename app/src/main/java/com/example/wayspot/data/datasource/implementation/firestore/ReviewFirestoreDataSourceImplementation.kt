
package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImplementation @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun createReview(
        review: ReviewFirestoreDto
    ): ReviewFirestoreDto {

        val docRef = db.collection("reviews")
            .add(review)
            .await()

        return review.copy(id = docRef.id)
    }

    suspend fun getReviewsByPlaceId(
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

    suspend fun getReviewById(id: String): ReviewFirestoreDto {

        val docRef = db.collection("reviews").document(id)

        val snapshot = docRef.get().await()

        val review = snapshot.toObject(ReviewFirestoreDto::class.java)
            ?: throw Exception("No se pudo obtener la reseña")

        return review.copy(id = snapshot.id)
    }

    suspend fun updateReview(
        review: ReviewFirestoreDto
    ) {
        val docRef = db.collection("reviews").document(review.id)

        docRef.set(review).await()
    }


    suspend fun deleteReview(id: String) {
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

}
