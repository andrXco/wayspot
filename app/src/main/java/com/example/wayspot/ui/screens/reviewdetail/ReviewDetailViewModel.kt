package com.example.wayspot.ui.screens.reviewdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.AuthRepository
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
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewDetailState())
    val uiState: StateFlow<ReviewDetailState> = _uiState

    val currentUserId: String?
        get() = authRepository.currentUser?.uid


    fun loadReview(id: String) {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                comments = emptyList()
            )
        }

        viewModelScope.launch {
            val reviewResult = reviewRepository.getReviewById(id)

            if (reviewResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = reviewResult.exceptionOrNull()?.message
                    )
                }
                return@launch
            }

            val review = reviewResult.getOrNull() ?: return@launch

            val place = placeRepository.getPlaceById(review.placeId)
            val author = userRepository.getUserById(review.userId)
            val commentsResult = reviewRepository.getComments(id)


            _uiState.update {
                it.copy(
                    review = review,
                    place = place.getOrNull(),
                    author = author.getOrNull(),
                    comments = commentsResult.getOrNull().orEmpty(),
                    isLoading = false,
                    errorMessage = place.exceptionOrNull()?.message
                        ?: author.exceptionOrNull()?.message
                        ?: commentsResult.exceptionOrNull()?.message
                )
            }

        }
    }

    fun requestDelete() {
        val userId = authRepository.currentUser?.uid

        if (userId != null && _uiState.value.review?.userId == userId) {
            _uiState.update {
                it.copy(showDeleteConfirmation = true)
            }
        }
    }

    fun dismissDelete() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }

    fun deleteReview() {
        val review = _uiState.value.review ?: return

        val userId = authRepository.currentUser?.uid ?: return

        if (review.userId != userId || _uiState.value.isDeleting) {
            return
        }

        _uiState.update {
            it.copy(
                isDeleting = true,
                showDeleteConfirmation = false
            )
        }

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
