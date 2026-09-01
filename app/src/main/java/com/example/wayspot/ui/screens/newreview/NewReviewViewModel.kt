package com.example.wayspot.ui.screens.newreview

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.data.model.ReviewRules
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NewReviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewReviewState())

    val uiState: StateFlow<NewReviewState> = _uiState

    fun loadDraft(
        placeId: String,
        initialDraft: ReviewDraft?
    ) {
        val validInitialDraft = ReviewRules.normalizeDraft(
            draft = initialDraft?.takeIf { draft ->
                draft.placeId == placeId
            } ?: ReviewRules.emptyDraft(placeId),
            placeId = placeId
        )

        _uiState.update { currentState ->
            currentState.copy(
                rating = validInitialDraft.rating,
                title = validInitialDraft.title,
                description = validInitialDraft.description,
                photoUris = validInitialDraft.photoUris
            )
        }
    }

    fun updateRating(selectedRating: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                rating = selectedRating.coerceIn(
                    ReviewRules.MIN_RATING,
                    ReviewRules.MAX_RATING
                )
            )
        }
    }

    fun updateTitle(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                title = input.take(
                    ReviewRules.MAX_TITLE_LENGTH
                )
            )
        }
    }

    fun updateDescription(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                description = input.take(
                    ReviewRules.MAX_DESCRIPTION_LENGTH
                )
            )
        }
    }

    fun addPhoto(photoUri: String) {
        val currentPhotos = _uiState.value.photoUris

        if (
            currentPhotos.size < ReviewRules.MAX_PHOTOS &&
            photoUri !in currentPhotos
        ) {
            _uiState.update { currentState ->
                currentState.copy(
                    photoUris = currentPhotos + photoUri
                )
            }
        }
    }

    fun removePhoto(photoUri: String) {
        _uiState.update { currentState ->
            currentState.copy(
                photoUris = currentState.photoUris.filterNot {
                    it == photoUri
                }
            )
        }
    }
}