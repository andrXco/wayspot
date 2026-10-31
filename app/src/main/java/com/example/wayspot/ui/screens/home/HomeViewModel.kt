package com.example.wayspot.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.home.HomeCategoryId
import com.example.wayspot.data.model.home.HomeRules
import com.example.wayspot.data.model.savedplace.SavedPlace
import com.example.wayspot.data.model.savedplace.SavedPlacesRules
import com.example.wayspot.data.model.review.ReviewFeedItem
import com.example.wayspot.data.model.user.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.data.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect


/**
 * Carga el contenido de inicio y transforma las interacciones del carrusel y las reseñas en
 * actualizaciones inmutables de [HomeState].
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState
    private var homeLoadJob: Job? = null
    private var followingLoadJob: Job? = null

    fun refresh() {
        if (!_uiState.value.isLoading) loadHome()
    }

    private fun loadHome() {
        homeLoadJob?.cancel()
        val selectedCategory = _uiState.value.selectedCategory ?: HomeCategoryId.NATURE

        _uiState.update { currentState ->
            currentState.copy(
                selectedCategory = selectedCategory,
                categories = PreviewData.homeCategories,
                featuredPlans = HomeRules.filterFeaturedPlans(
                    plans = PreviewData.homeFeaturedPlans,
                    categoryId = selectedCategory
                ),
                reviews = emptyList(),
                featuredLikeCounts = PreviewData.homeFeaturedPlans.associate { plan ->
                    plan.id to plan.initialLikeCount
                },
                reviewLikeCounts = emptyMap(),
                isLoading = true,
                errorMessage = null
            )
        }


        homeLoadJob = viewModelScope.launch {
            val placesResult = placeRepository.getPlaces()
            val places = placesResult.getOrNull()
            if (places == null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = placesResult.exceptionOrNull()?.message
                            ?: "No se pudieron cargar los lugares"
                    )
                }
                return@launch
            }

            val usersResult = userRepository.getUsers()
            val users = usersResult.getOrNull().orEmpty()
            _uiState.update { currentState ->
                currentState.copy(places = places)
            }

            reviewRepository.getAllReviews()
                .catch { error ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = error.message
                                ?: "No se pudieron escuchar las reseñas"
                        )
                    }
                }
                .collect { reviews ->
                    val feed = mutableListOf<ReviewFeedItem>()

                    for (index in reviews.size - 1 downTo 0) {
                        val review = reviews[index]
                        val place = places.find { it.id == review.placeId }
                        val storedAuthor = users.find { it.id == review.userId }
                        val author = if (
                            !review.authorName.isNullOrBlank() &&
                            !review.authorUsername.isNullOrBlank()
                        ) {
                            UserInfo(
                                id = review.userId,
                                name = review.authorName,
                                username = review.authorUsername,
                                bio = storedAuthor?.bio,
                                location = storedAuthor?.location,
                                avatarUrl = review.authorAvatarUrl
                                    ?.takeIf { it.isNotBlank() }
                                    ?: storedAuthor?.avatarUrl
                            )
                        } else {
                            storedAuthor
                        }

                        if (place != null && author != null) {
                            feed.add(ReviewFeedItem(review, place, author))
                        }
                    }

                    val needsMissingAuthor = reviews.any { review ->
                        (review.authorName.isNullOrBlank() || review.authorUsername.isNullOrBlank()) &&
                            users.none { user -> user.id == review.userId }
                    }
                    if (needsMissingAuthor && usersResult.isFailure) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                errorMessage = usersResult.exceptionOrNull()?.message
                                    ?: "No se pudieron cargar los usuarios"
                            )
                        }
                        return@collect
                    }

                    val likedReviewsResult = reviewRepository.getLikedReviewIds(
                        feed.map { item -> item.review.id }
                    )
                    val likedReviewIds = likedReviewsResult.getOrNull()
                    if (likedReviewIds == null) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                errorMessage = likedReviewsResult.exceptionOrNull()?.message
                                    ?: "No se pudieron cargar los likes"
                            )
                        }
                        return@collect
                    }

                    val feedWithLikes = feed.map { item ->
                        item.copy(
                            review = item.review.copy(
                                liked = item.review.id in likedReviewIds
                            )
                        )
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            reviews = feedWithLikes,
                            likedReviewIds = likedReviewIds,
                            reviewLikeCounts = feedWithLikes.associate { item ->
                                item.review.id to item.review.likesCount
                            },
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun toggleFollowingFilter() {
        val currentState = _uiState.value
        if (currentState.isLoadingFollowingUsers) return

        if (currentState.isFollowingOnly) {
            _uiState.update { state ->
                state.copy(
                    isFollowingOnly = false,
                    followingUsersLoadFailed = false
                )
            }
            return
        }

        val currentUserId = authRepository.currentUser?.uid
        if (currentUserId.isNullOrBlank()) {
            _uiState.update { state ->
                state.copy(followingUsersLoadFailed = true)
            }
            return
        }

        followingLoadJob?.cancel()
        _uiState.update { state ->
            state.copy(
                isFollowingOnly = true,
                isLoadingFollowingUsers = true,
                followingUsersLoadFailed = false
            )
        }

        followingLoadJob = viewModelScope.launch {
            val result = userRepository.getFollowingUserIds(currentUserId)
            result.fold(
                onSuccess = { userIds ->
                    _uiState.update { state ->
                        state.copy(
                            followingUserIds = userIds,
                            isLoadingFollowingUsers = false,
                            followingUsersLoadFailed = false
                        )
                    }
                },
                onFailure = {
                    _uiState.update { state ->
                        state.copy(
                            isFollowingOnly = false,
                            isLoadingFollowingUsers = false,
                            followingUsersLoadFailed = true
                        )
                    }
                }
            )
        }
    }

        fun updateSearchQuery(query: String) {
            _uiState.update { currentState ->
                currentState.copy(searchQuery = query)
            }
        }

        fun selectCategory(categoryId: HomeCategoryId) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedCategory = categoryId,
                    featuredPlans = HomeRules.filterFeaturedPlans(
                        plans = PreviewData.homeFeaturedPlans,
                        categoryId = categoryId
                    ),
                    activeFeaturedPlanIndex = 0
                )
            }
        }

        fun showNextFeaturedPlan() {
            _uiState.update { currentState ->
                currentState.copy(
                    activeFeaturedPlanIndex = HomeRules.nextCircularIndex(
                        currentIndex = currentState.activeFeaturedPlanIndex,
                        size = currentState.places.size
                    )
                )
            }
        }

        fun showPreviousFeaturedPlan() {
            _uiState.update { currentState ->
                currentState.copy(
                    activeFeaturedPlanIndex = HomeRules.previousCircularIndex(
                        currentIndex = currentState.activeFeaturedPlanIndex,
                        size = currentState.places.size
                    )
                )
            }
        }

        fun toggleFeaturedLike(planId: String) {
            _uiState.update { currentState ->
                val wasLiked = planId in currentState.likedFeaturedPlanIds
                currentState.copy(
                    likedFeaturedPlanIds = currentState.likedFeaturedPlanIds.toggle(planId),
                    featuredLikeCounts = currentState.featuredLikeCounts.withAdjustedCount(
                        id = planId,
                        delta = if (wasLiked) -1 else 1
                    )
                )
            }
        }

    fun toggleReviewLike(reviewId: String) {
        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewLike(reviewId)

            if (result.isSuccess) {
                _uiState.update { currentState ->
                    val wasLiked = reviewId in currentState.likedReviewIds

                    currentState.copy(
                        likedReviewIds = currentState.likedReviewIds.toggle(reviewId),
                        reviewLikeCounts = currentState.reviewLikeCounts.withAdjustedCount(
                            id = reviewId,
                            delta = if (wasLiked) -1 else 1
                        ),
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

        fun updateSavedPlaces(savedPlaces: List<SavedPlace>) {
            val savedPlaceIds = SavedPlacesRules.savedPlaceIds(savedPlaces)
            _uiState.update { currentState ->
                if (currentState.savedPlaceIds == savedPlaceIds) {
                    currentState
                } else {
                    currentState.copy(savedPlaceIds = savedPlaceIds)
                }
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
