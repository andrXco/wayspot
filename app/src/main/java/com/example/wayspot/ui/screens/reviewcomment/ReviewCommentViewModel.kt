package com.example.wayspot.ui.screens.reviewcomment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.ReviewCommentRules
import com.example.wayspot.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Administra el borrador y la publicación remota de un comentario. */
@HiltViewModel
class ReviewCommentViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewCommentState())
    val uiState: StateFlow<ReviewCommentState> = _uiState

    fun setReviewId(reviewId: String) {
        _uiState.update { currentState ->
            if (currentState.reviewId == reviewId) {
                currentState
            } else {
                currentState.copy(
                    reviewId = reviewId,
                    draft = "",
                    isPublishing = false,
                    isPublished = false,
                    errorMessage = null
                )
            }
        }
    }

    fun updateDraft(value: String) {
        _uiState.update { it.copy(draft = value, errorMessage = null) }
    }

    fun publishComment() {
        val state = _uiState.value
        val reviewId = state.reviewId ?: return
        val content = ReviewCommentRules.normalizeDraft(state.draft)
        if (!ReviewCommentRules.canPublish(content) || state.isPublishing) return

        _uiState.update { it.copy(isPublishing = true, errorMessage = null) }
        viewModelScope.launch {
            val result = reviewRepository.createComment(reviewId, content)
            _uiState.update {
                if (result.isSuccess) {
                    it.copy(isPublishing = false, isPublished = true)
                } else {
                    it.copy(
                        isPublishing = false,
                        errorMessage = result.exceptionOrNull()?.message.orEmpty()
                    )
                }
            }
        }
    }

    fun consumePublished() {
        _uiState.update { it.copy(isPublished = false) }
    }
}
