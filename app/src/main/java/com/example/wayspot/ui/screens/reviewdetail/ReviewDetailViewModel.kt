package com.example.wayspot.ui.screens.reviewdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.dto.BackendSession
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ReviewDetailViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val placeRepository: PlaceRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewDetailState())
    val uiState: StateFlow<ReviewDetailState> = _uiState

    fun loadReview(id: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val reviewResult = reviewRepository.getReviewById(id)
            if (reviewResult.isFailure) {
                _uiState.update { it.copy(isLoading = false, errorMessage = reviewResult.exceptionOrNull()?.message) }
                return@launch
            }
            val review = reviewResult.getOrNull() ?: return@launch
            val place = placeRepository.getPlaceById(review.placeId)
            val author = userRepository.getUserById(review.userId)
            val comments = reviewRepository.getComments(id)
            _uiState.update {
                it.copy(
                    review = review,
                    place = place.getOrNull(),
                    author = author.getOrNull(),
                    comments = comments.getOrNull().orEmpty(),
                    isLoading = false,
                    errorMessage = place.exceptionOrNull()?.message
                        ?: author.exceptionOrNull()?.message
                        ?: comments.exceptionOrNull()?.message
                )
            }
        }
    }

    fun requestDelete() {
        if (_uiState.value.review?.userId == BackendSession.USER_ID) {
            _uiState.update { it.copy(showDeleteConfirmation = true) }
        }
    }

    fun dismissDelete() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }

    fun deleteReview() {
        val review = _uiState.value.review ?: return
        if (review.userId != BackendSession.USER_ID || _uiState.value.isDeleting) return
        _uiState.update { it.copy(isDeleting = true, showDeleteConfirmation = false) }
        viewModelScope.launch {
            val result = reviewRepository.deleteReview(review.id)
            _uiState.update {
                it.copy(
                    isDeleting = false,
                    isDeleted = result.isSuccess,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
}
