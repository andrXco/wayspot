package com.example.wayspot.ui.screens.newreview

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.data.model.ReviewRules
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NewReviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewReviewState())

    val uiState: StateFlow<NewReviewState> = _uiState

    fun loadReview(
        placeId: String,
        initialDraft: ReviewDraft?
    ) {
        val place = PreviewDataPopular.listPlaces.find { candidate ->
            candidate.id == placeId
        }

        if (place == null) {
            _uiState.value = NewReviewState()
            return
        }

        val validInitialDraft = ReviewRules.normalizeDraft(
            draft = initialDraft?.takeIf { draft ->
                draft.placeId == placeId
            } ?: ReviewRules.emptyDraft(placeId),
            placeId = placeId
        )

        _uiState.update { currentState ->
            currentState.copy(
                place = place,
                reviewDraft = validInitialDraft,
                isPublishEnabled = ReviewRules.canPublish(validInitialDraft)
            )
        }
    }

    fun updateRating(selectedRating: Int) {
        updateDraft { currentDraft ->
            currentDraft.copy(
                rating = selectedRating.coerceIn(
                    ReviewRules.MIN_RATING,
                    ReviewRules.MAX_RATING
                )
            )
        }
    }

    fun updateTitle(input: String) {
        updateDraft { currentDraft ->
            currentDraft.copy(
                title = input.take(
                    ReviewRules.MAX_TITLE_LENGTH
                )
            )
        }
    }

    fun updateDescription(input: String) {
        updateDraft { currentDraft ->
            currentDraft.copy(
                description = input.take(
                    ReviewRules.MAX_DESCRIPTION_LENGTH
                )
            )
        }
    }

    fun addPhoto(photoUri: String) {
        updateDraft { currentDraft ->
            if (
                currentDraft.photoUris.size < ReviewRules.MAX_PHOTOS &&
                photoUri !in currentDraft.photoUris
            ) {
                currentDraft.copy(
                    photoUris = currentDraft.photoUris + photoUri
                )
            } else {
                currentDraft
            }
        }
    }

    fun removePhoto(photoUri: String) {
        updateDraft { currentDraft ->
            currentDraft.copy(
                photoUris = currentDraft.photoUris.filterNot { currentUri ->
                    currentUri == photoUri
                }
            )
        }
    }

    fun draftForSaving(): ReviewDraft? = _uiState.value.reviewDraft

    fun draftForPublishing(): ReviewDraft? = _uiState.value.reviewDraft?.let(
        ReviewRules::prepareForPublish
    )

    private fun updateDraft(transform: (ReviewDraft) -> ReviewDraft) {
        _uiState.update { currentState ->
            val currentDraft = currentState.reviewDraft
                ?: return@update currentState
            val updatedDraft = ReviewRules.normalizeDraft(
                transform(currentDraft)
            )

            currentState.copy(
                reviewDraft = updatedDraft,
                isPublishEnabled = ReviewRules.canPublish(updatedDraft)
            )
        }
    }
}
