package com.example.wayspot.ui.screens.publicprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.review.ReviewFeedItem
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.user.UserInfo
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
class PublicProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val placeRepository: PlaceRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(PublicProfileState())
    val uiState: StateFlow<PublicProfileState> = _uiState
    private var loadGeneration = 0L

    fun loadUser(id: String) {
        val requestGeneration = ++loadGeneration
        val currentUserId = authRepository.currentUser?.uid
        _uiState.update {
            it.copy(
                isLoading = true,
                hasDataLoadError = false,
                errorMessage = null,
                hasFollowError = false,
                user = null,
                reviews = emptyList(),
                reviewFeedItems = emptyList(),
                reviewLikeCounts = emptyMap(),
                likedReviewIds = emptySet(),
                expandedReviewIds = emptySet(),
                areReviewLikesLoaded = false,
                canFollow = currentUserId != null && currentUserId != id,
                isFollowing = false,
                isFollowActionInProgress = false,
                followersCount = 0,
                followingCount = 0
            )
        }
        viewModelScope.launch {
            val user = userRepository.getUserById(id)
            if (requestGeneration != loadGeneration) return@launch
            if (user.isFailure) {
                _uiState.update {
                    if (requestGeneration == loadGeneration) {
                        it.copy(
                            isLoading = false,
                            hasDataLoadError = true,
                            errorMessage = user.exceptionOrNull()?.message
                        )
                    } else {
                        it
                    }
                }
                return@launch
            }
            val reviewsResult = userRepository.getReviewsByUserId(id)
            val placesResult = placeRepository.getPlaces()
            val reviews = reviewsResult.getOrNull().orEmpty()
            val likedReviewsResult = reviewRepository.getLikedReviewIds(
                reviews.map(ReviewInfo::id)
            )
            val likedReviewIds = likedReviewsResult.getOrNull().orEmpty()
            val reviewsWithLikes = reviews.map { review ->
                review.copy(liked = review.id in likedReviewIds)
            }
            val profile = user.getOrNull()
            val places = placesResult.getOrNull().orEmpty()
            val reviewFeedItems = if (placesResult.isSuccess && profile != null) {
                reviewsWithLikes.mapNotNull { review ->
                    val place = places.firstOrNull { it.id == review.placeId }
                        ?: return@mapNotNull null
                    val author = UserInfo(
                        id = review.userId,
                        name = review.authorName?.takeIf(String::isNotBlank)
                            ?: profile.name,
                        username = review.authorUsername?.takeIf(String::isNotBlank)
                            ?: profile.username,
                        bio = profile.bio,
                        location = profile.location,
                        avatarUrl = review.authorAvatarUrl?.takeIf(String::isNotBlank)
                            ?: profile.avatarUrl
                    )
                    ReviewFeedItem(
                        review = review,
                        place = place,
                        author = author
                    )
                }
            } else {
                emptyList()
            }
            val isFollowing = currentUserId
                ?.takeIf { it != id }
                ?.let { userRepository.isFollowing(it, id).getOrNull() }
                ?: false
            val followersCount = userRepository.getFollowersCount(id).getOrNull() ?: 0
            val followingCount = userRepository.getFollowingCount(id).getOrNull() ?: 0
            if (requestGeneration != loadGeneration) return@launch
            val errorMessage = listOfNotNull(
                reviewsResult.exceptionOrNull()?.message,
                placesResult.exceptionOrNull()?.message,
                likedReviewsResult.exceptionOrNull()?.message
            ).joinToString("\n").takeIf(String::isNotBlank)
            _uiState.update {
                if (requestGeneration == loadGeneration) {
                    it.copy(
                        user = profile,
                        reviews = reviewsWithLikes,
                        reviewFeedItems = reviewFeedItems,
                        reviewLikeCounts = reviewsWithLikes.associate { review ->
                            review.id to review.likesCount
                        },
                        likedReviewIds = likedReviewIds,
                        areReviewLikesLoaded = likedReviewsResult.isSuccess,
                        isFollowing = isFollowing,
                        followersCount = followersCount,
                        followingCount = followingCount,
                        isLoading = false,
                        hasDataLoadError = reviewsResult.isFailure ||
                            placesResult.isFailure || likedReviewsResult.isFailure,
                        errorMessage = errorMessage
                    )
                } else {
                    it
                }
            }
        }
    }

    fun toggleFollow() {
        val state = _uiState.value
        val followerId = authRepository.currentUser?.uid ?: return
        val followedId = state.user?.id ?: return
        if (!state.canFollow || state.isFollowActionInProgress || followedId.isBlank()) return

        val nextFollowingState = !state.isFollowing
        _uiState.update {
            it.copy(
                isFollowActionInProgress = true,
                hasFollowError = false
            )
        }

        viewModelScope.launch {
            val result = userRepository.followOrUnfollowUser(
                currentUserId = followerId,
                targetUserId = followedId
            )
            if (result.isSuccess) {
                val refreshedCount = userRepository.getFollowersCount(followedId).getOrNull()
                _uiState.update {
                    if (it.user?.id == followedId) {
                        it.copy(
                            isFollowing = nextFollowingState,
                            followersCount = refreshedCount
                                ?: (it.followersCount + if (nextFollowingState) 1 else -1)
                                    .coerceAtLeast(0),
                            isFollowActionInProgress = false
                        )
                    } else {
                        it
                    }
                }
            } else {
                _uiState.update {
                    if (it.user?.id == followedId) {
                        it.copy(
                            isFollowActionInProgress = false,
                            hasFollowError = true
                        )
                    } else {
                        it
                    }
                }
            }
        }
    }

    fun toggleReviewLike(reviewId: String) {
        val requestedUserId = _uiState.value.user?.id ?: return
        if (_uiState.value.reviews.none { it.id == reviewId }) return

        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewLike(reviewId)
            if (result.isSuccess) {
                _uiState.update { currentState ->
                    if (currentState.user?.id != requestedUserId) return@update currentState

                    val wasLiked = reviewId in currentState.likedReviewIds
                    val nextLikeCount = (
                        (currentState.reviewLikeCounts[reviewId] ?: 0) +
                            (if (wasLiked) -1 else 1)
                        ).coerceAtLeast(0)
                    val nextLikedReviewIds = if (wasLiked) {
                        currentState.likedReviewIds - reviewId
                    } else {
                        currentState.likedReviewIds + reviewId
                    }

                    currentState.copy(
                        reviews = currentState.reviews.map { review ->
                            if (review.id == reviewId) {
                                review.copy(
                                    liked = !wasLiked,
                                    likesCount = nextLikeCount
                                )
                            } else {
                                review
                            }
                        },
                        reviewFeedItems = currentState.reviewFeedItems.map { item ->
                            if (item.review.id == reviewId) {
                                item.copy(
                                    review = item.review.copy(
                                        liked = !wasLiked,
                                        likesCount = nextLikeCount
                                    )
                                )
                            } else {
                                item
                            }
                        },
                        likedReviewIds = nextLikedReviewIds,
                        reviewLikeCounts = currentState.reviewLikeCounts +
                            (reviewId to nextLikeCount),
                        errorMessage = null
                    )
                }
            } else {
                _uiState.update {
                    if (it.user?.id == requestedUserId) {
                        it.copy(errorMessage = result.exceptionOrNull()?.message)
                    } else {
                        it
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
