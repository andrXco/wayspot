package com.example.wayspot.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.review.ReviewFeedItem
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.model.user.UserProfile
import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/** Expone los datos del perfil y delega el cierre de sesión al repositorio de autenticación. */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val placeRepository: PlaceRepository
) : ViewModel() {



    private val _uiState = MutableStateFlow(ProfileState())

    val uiState: StateFlow<ProfileState> = _uiState


    fun loadReviews() {
        val userId = authRepository.currentUser?.uid

        if (userId == null) {
            _uiState.update {
                it.copy(
                    reviews = emptyList(),
                    reviewFeedItems = emptyList(),
                    isLoading = false,
                    errorMessage = "Usuario no autenticado"
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            val result = userRepository.getReviewsByUserId(userId)
            val placesResult = placeRepository.getPlaces()
            val reviews = result.getOrNull().orEmpty()
            val places = placesResult.getOrNull().orEmpty()
            val profile = _uiState.value.userProfile
            val reviewFeedItems = reviews.mapNotNull { review ->
                val place = places.firstOrNull { it.id == review.placeId } ?: return@mapNotNull null
                val author = UserInfo(
                    id = review.userId,
                    name = review.authorName?.takeIf(String::isNotBlank)
                        ?: profile?.name.orEmpty(),
                    username = review.authorUsername?.takeIf(String::isNotBlank)
                        ?: profile?.username.orEmpty(),
                    bio = profile?.bio,
                    location = profile?.location,
                    avatarUrl = review.authorAvatarUrl?.takeIf(String::isNotBlank)
                        ?: profile?.avatarUrl
                )
                ReviewFeedItem(review = review, place = place, author = author)
            }

            _uiState.update { currentState ->
                currentState.copy(
                    reviews = reviews,
                    reviewFeedItems = reviewFeedItems,
                    reviewLikeCounts = reviews.associate { review ->
                        review.id to (currentState.reviewLikeCounts[review.id] ?: 0)
                    },
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun loadProfile(profile: UserProfile) {

        _uiState.update { currentState ->
            currentState.copy(
                userProfile = profile
            )
        }
    }
    fun signOut() {
        authRepository.signOut()
    }

    fun toggleReviewLike(reviewId: String) {
        _uiState.update { currentState ->
            val wasLiked = reviewId in currentState.likedReviewIds
            currentState.copy(
                likedReviewIds = currentState.likedReviewIds.toggle(reviewId),
                reviewLikeCounts = currentState.reviewLikeCounts.withAdjustedCount(
                    id = reviewId,
                    delta = if (wasLiked) -1 else 1
                )
            )
        }
    }

    fun toggleReviewExpanded(reviewId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                expandedReviewIds = currentState.expandedReviewIds.toggle(reviewId)
            )
        }
    }

    fun toggleReviewShared(reviewId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                sharedReviewIds = currentState.sharedReviewIds.toggle(reviewId)
            )
        }
    }

    private fun Set<String>.toggle(id: String): Set<String> = if (id in this) {
        this - id
    } else {
        this + id
    }

    private fun Map<String, Int>.withAdjustedCount(
        id: String,
        delta: Int
    ): Map<String, Int> = this + (id to ((this[id] ?: 0) + delta).coerceAtLeast(0))
}
