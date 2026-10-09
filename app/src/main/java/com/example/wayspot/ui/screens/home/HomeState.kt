package com.example.wayspot.ui.screens.home

import com.example.wayspot.data.model.HomeCategory
import com.example.wayspot.data.model.HomeCategoryId
import com.example.wayspot.data.model.HomeFeaturedPlan
import com.example.wayspot.data.model.ReviewFeedItem
import com.example.wayspot.data.model.PlaceInfo

/** Estado inmutable de la experiencia de inicio, incluidas sus interacciones locales de feed. */
data class HomeState(
    val searchQuery: String = "",
    val selectedCategory: HomeCategoryId? = null,
    val categories: List<HomeCategory> = emptyList(),
    val featuredPlans: List<HomeFeaturedPlan> = emptyList(),
    val reviews: List<ReviewFeedItem> = emptyList(),
    val places: List<PlaceInfo> = emptyList(),
    val activeFeaturedPlanIndex: Int = 0,
    val featuredLikeCounts: Map<String, Int> = emptyMap(),
    val reviewLikeCounts: Map<String, Int> = emptyMap(),
    val likedFeaturedPlanIds: Set<String> = emptySet(),
    val likedReviewIds: Set<String> = emptySet(),
    val expandedReviewIds: Set<String> = emptySet(),
    val sharedReviewIds: Set<String> = emptySet(),
    val savedPlaceIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
