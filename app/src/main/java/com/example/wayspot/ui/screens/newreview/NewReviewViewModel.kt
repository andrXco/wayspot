package com.example.wayspot.ui.screens.newreview

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.data.model.ReviewRules
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Mantiene el borrador de reseña normalizado y deriva cuándo puede publicarse. */
@HiltViewModel
class NewReviewViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(NewReviewState())

    val uiState: StateFlow<NewReviewState> = _uiState

    fun loadReview(placeId: String?) {
        val places = PreviewDataPopular.listPlaces
        _uiState.update { currentState ->
            if (placeId == null) {
                currentState.copy(places = places)
            } else {
                selectPlace(
                    currentState = currentState.copy(places = places),
                    placeId = placeId
                )
            }
        }
    }

    fun selectPlace(placeId: String) {
        _uiState.update { currentState ->
            selectPlace(
                currentState = currentState,
                placeId = placeId
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

    fun draftForPublishing(): ReviewDraft? = _uiState.value.reviewDraft?.let(
        ReviewRules::prepareForPublish
    )

    private fun selectPlace(
        currentState: NewReviewState,
        placeId: String
    ): NewReviewState {
        val place = currentState.places.find { candidate ->
            candidate.id == placeId
        } ?: return currentState

        if (
            currentState.place?.id == placeId &&
            currentState.reviewDraft?.placeId == placeId
        ) {
            return currentState
        }

        val reviewDraft = ReviewRules.emptyDraft(placeId)

        return currentState.copy(
            place = place,
            reviewDraft = reviewDraft,
            isPublishEnabled = ReviewRules.canPublish(reviewDraft)
        )
    }

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
