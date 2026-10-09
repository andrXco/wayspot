package com.example.wayspot.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.HomeCategoryId
import com.example.wayspot.data.model.HomeRules
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.SavedPlacesRules
import com.example.wayspot.data.model.ReviewFeedItem
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


/**
 * Carga el contenido de inicio y transforma las interacciones del carrusel y las reseñas en
 * actualizaciones inmutables de [HomeState].
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    fun refresh() {
        if (!_uiState.value.isLoading) loadHome()
    }

    private fun loadHome() {
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

        viewModelScope.launch {

            val placesResult = placeRepository.getPlaces()
            val reviewsResult = reviewRepository.getReviews()
            val usersResult = userRepository.getUsers()

            val error = placesResult.exceptionOrNull()
                ?: reviewsResult.exceptionOrNull()
                ?: usersResult.exceptionOrNull()
            if (error != null) {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, errorMessage = error.message)
                }
                return@launch
            }

            val places = placesResult.getOrNull().orEmpty()
            val reviews = reviewsResult.getOrNull().orEmpty()
            val users = usersResult.getOrNull().orEmpty()
            val feed = mutableListOf<ReviewFeedItem>()

            for (index in reviews.size - 1 downTo 0) {
                val review = reviews[index]
                val place = places.find { it.id == review.placeId }
                val author = users.find { it.id.toString() == review.userId }
                if (place != null && author != null) {
                    feed.add(ReviewFeedItem(review, place, author))
                }
            }

            _uiState.update { currentState ->
                currentState.copy(
                    places = places,
                    reviews = feed,
                    isLoading = false,
                    errorMessage = null
                )
            }
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
