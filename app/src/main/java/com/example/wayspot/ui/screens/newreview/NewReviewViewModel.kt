package com.example.wayspot.ui.screens.newreview

import androidx.lifecycle.ViewModel
import com.example.wayspot.R
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.data.model.ReviewRules
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Mantiene el borrador de reseña normalizado y deriva cuándo puede publicarse. */
@HiltViewModel
class NewReviewViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewReviewState())

    val uiState: StateFlow<NewReviewState> = _uiState

    fun loadReview(placeId: String?, reviewId: String?) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, errorResId = null, editingReviewId = reviewId) }
        viewModelScope.launch {
            val placesResult = placeRepository.getPlaces()
            if (placesResult.isFailure) {
                _uiState.update { it.copy(isLoading = false, errorMessage = placesResult.exceptionOrNull()?.message) }
                return@launch
            }
            val places = placesResult.getOrNull() ?: return@launch
            val existing = reviewId?.let { id ->
                val result = reviewRepository.getReviewById(id)
                if (result.isFailure) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message) }
                    return@launch
                }
                result.getOrNull()
            }
            if (existing != null && existing.userId != "1") {
                _uiState.update { it.copy(isLoading = false, errorResId = R.string.review_edit_forbidden) }
                return@launch
            }
            val selectedId = existing?.placeId ?: placeId
            val selectedPlace = places.find { it.id == selectedId }
            val draft = if (existing != null) {
                ReviewDraft(existing.placeId, existing.rating, existing.title, existing.description, emptyList())
            } else {
                selectedPlace?.let { ReviewRules.emptyDraft(it.id) }
            }
            _uiState.update {
                it.copy(
                    places = places,
                    place = selectedPlace,
                    reviewDraft = draft,
                    isPublishEnabled = draft?.let(ReviewRules::canPublish) ?: false,
                    isLoading = false
                )
            }
        }
    }

    fun publish() {
        val draft = _uiState.value.reviewDraft?.let(ReviewRules::prepareForPublish) ?: return
        if (_uiState.value.isPublishing) return
        _uiState.update { it.copy(isPublishing = true, errorMessage = null, errorResId = null) }
        viewModelScope.launch {
            val result = reviewRepository.saveReview(draft, _uiState.value.editingReviewId)
            _uiState.update {
                it.copy(
                    isPublishing = false,
                    isSaved = result.isSuccess,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun consumeSaved() {
        _uiState.update { it.copy(isSaved = false) }
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
