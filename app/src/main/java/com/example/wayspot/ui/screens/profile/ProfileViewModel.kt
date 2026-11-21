package com.example.wayspot.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.review.ReviewFeedItem
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.model.user.UserProfile
import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.UserRepository
import com.example.wayspot.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


/** Expone los datos del perfil y delega el cierre de sesión al repositorio de autenticación. */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val placeRepository: PlaceRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {



    private val _uiState = MutableStateFlow(ProfileState())
    private val reviewUpdates = Mutex()

    val uiState: StateFlow<ProfileState> = _uiState


    fun loadReviews() {
        viewModelScope.launch {
            reviewUpdates.withLock {
                val userId = authRepository.currentUser?.uid
                if (userId == null) {
                    _uiState.update { state ->
                        ProfileState(
                            userProfile = state.userProfile,
                            errorMessage = "Usuario no autenticado"
                        )
                    }
                    return@withLock
                }

                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val result = userRepository.getReviewsByUserId(userId)
                ensureActive()
                val reviews = result.getOrNull()
                if (reviews == null) {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.exceptionOrNull()?.message
                                ?: "No se pudieron cargar las reseñas"
                        )
                    }
                    return@withLock
                }

                val places = placeRepository.getPlaces().getOrNull().orEmpty()
                val likedReviewsResult = reviewRepository.getLikedReviewIds(reviews.map { it.id })
                ensureActive()
                val likedReviewIds = likedReviewsResult.getOrNull().orEmpty()
                val reviewsWithLikes = reviews.map { review ->
                    review.copy(liked = review.id in likedReviewIds)
                }

                val profile = _uiState.value.userProfile
                val placesById = places.associateBy { it.id }
                val reviewFeedItems = reviewsWithLikes.mapNotNull { review ->
                    val place = review.placeId?.let(placesById::get)
                    if (place == null && review.coordinates?.isValid != true) return@mapNotNull null
                    val author = UserInfo(
                        id = review.userId,
                        name = review.authorName?.takeIf(String::isNotBlank) ?: profile?.name.orEmpty(),
                        username = review.authorUsername?.takeIf(String::isNotBlank)
                            ?: profile?.username.orEmpty(),
                        bio = profile?.bio,
                        location = profile?.location,
                        avatarUrl = review.authorAvatarUrl?.takeIf(String::isNotBlank)
                            ?: profile?.avatarUrl
                    )
                    ReviewFeedItem(review = review, place = place, author = author)
                }

                _uiState.update { state ->
                    state.copy(
                        reviews = reviewsWithLikes,
                        reviewFeedItems = reviewFeedItems,
                        areReviewsLoaded = true,
                        areReviewLikesLoaded = likedReviewsResult.isSuccess,
                        isLoading = false,
                        errorMessage = likedReviewsResult.exceptionOrNull()?.message
                    )
                }
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
        viewModelScope.launch {
            reviewUpdates.withLock {
                val state = _uiState.value
                if (!state.areReviewLikesLoaded) return@withLock
                val review = state.reviews.firstOrNull { it.id == reviewId } ?: return@withLock

                val result = reviewRepository.sendOrDeleteReviewLike(reviewId)
                ensureActive()
                if (result.isSuccess) {
                    val delta = if (review.liked) -1 else 1
                    val updatedReview = review.copy(
                        likesCount = (review.likesCount + delta).coerceAtLeast(0),
                        liked = !review.liked
                    )
                    _uiState.update { currentState ->
                        currentState.copy(
                            reviews = currentState.reviews.map {
                                if (it.id == reviewId) updatedReview else it
                            },
                            reviewFeedItems = currentState.reviewFeedItems.map {
                                if (it.review.id == reviewId) it.copy(review = updatedReview) else it
                            },
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = result.exceptionOrNull()?.message
                                ?: "No se pudo actualizar el like"
                        )
                    }
                }
            }
        }
    }


    fun toggleReviewExpanded(reviewId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                expandedReviewIds = currentState.expandedReviewIds.toggle(reviewId)
            )
        }
    }

    private fun Set<String>.toggle(id: String): Set<String> = if (id in this) {
        this - id
    } else {
        this + id
    }

}
