package com.example.wayspot.repository

import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.model.review.ReviewDraft
import com.example.wayspot.data.model.review.ReviewRules
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ReviewPhotoMappingTest {
    @Test
    fun normalizeDraftKeepsOnlyTwoDistinctPhotosInSelectionOrder() {
        val draft = ReviewDraft(
            placeId = "place-1",
            rating = 5,
            title = "",
            description = "",
            photoUris = listOf(
                "content://photo-1",
                "content://photo-1",
                "content://photo-2",
                "content://photo-3"
            )
        )

        val normalizedDraft = ReviewRules.normalizeDraft(draft)

        assertThat(normalizedDraft.photoUris)
            .containsExactly("content://photo-1", "content://photo-2")
            .inOrder()
    }

    @Test
    fun firestoreReviewMapsPhotoUrlsToDomainReview() {
        val photoUrls = listOf("https://storage/photo-1", "https://storage/photo-2")

        val review = ReviewFirestoreDto(photoUrls = photoUrls).toReviewInfo()

        assertThat(review.photoUrls).containsExactlyElementsIn(photoUrls).inOrder()
    }

    @Test
    fun legacyFirestoreReviewDefaultsToNoPhotos() {
        val review = ReviewFirestoreDto().toReviewInfo()

        assertThat(review.photoUrls).isEmpty()
    }
}
