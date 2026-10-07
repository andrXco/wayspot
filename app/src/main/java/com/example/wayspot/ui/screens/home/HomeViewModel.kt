package com.example.wayspot.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.HomeCategoryId
import com.example.wayspot.data.model.HomeRules
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.SavedPlacesRules
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import kotlinx.coroutines.launch


/**
 * Carga el contenido de inicio y transforma las interacciones del carrusel y las reseñas en
 * actualizaciones inmutables de [HomeState].
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        loadHome()
    }

    fun refresh() {
        if (!_uiState.value.isLoading) loadHome()
    }

    private fun loadHome() {
        val selectedCategory = HomeCategoryId.NATURE

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
                reviewCommentCounts = emptyMap()
            )
        }

        viewModelScope.launch {

            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val result = placeRepository.getPlaces()

            if (result.isSuccess) {
                val places = result.getOrNull()

                if (places != null) {
                    val feedResult = reviewRepository.getFeed(places)
                    _uiState.update { currentState ->
                        currentState.copy(
                            places = places,
                            reviews = feedResult.getOrNull().orEmpty(),
                            isLoading = false,
                            errorMessage = feedResult.exceptionOrNull()?.message
                        )
                    }
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                }
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

        fun registerReviewComment(reviewId: String) {
            _uiState.update { currentState ->
                if (reviewId in currentState.commentedReviewIds) {
                    currentState
                } else {
                    currentState.copy(
                        commentedReviewIds = currentState.commentedReviewIds + reviewId,
                        reviewCommentCounts = currentState.reviewCommentCounts.withAdjustedCount(
                            id = reviewId,
                            delta = 1
                        )
                    )
                }
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
