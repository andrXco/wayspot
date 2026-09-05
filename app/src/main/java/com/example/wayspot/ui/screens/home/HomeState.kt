package com.example.wayspot.ui.screens.home

import com.example.wayspot.data.model.HomeCategory
import com.example.wayspot.data.model.HomeCategoryId
import com.example.wayspot.data.model.HomeFeaturedPlan
import com.example.wayspot.data.model.HomeReview
import com.example.wayspot.data.model.Place

data class HomeState(
    val searchQuery: String = "",
    val selectedCategory: HomeCategoryId? = null,
    val categories: List<HomeCategory> = emptyList(),
    val featuredPlans: List<HomeFeaturedPlan> = emptyList(),
    val reviews: List<HomeReview> = emptyList(),
    val places: List<Place> = emptyList(),
    val activeFeaturedPlanIndex: Int = 0,
    val featuredLikeCounts: Map<String, Int> = emptyMap(),
    val reviewLikeCounts: Map<String, Int> = emptyMap(),
    val reviewCommentCounts: Map<String, Int> = emptyMap(),
    val likedFeaturedPlanIds: Set<String> = emptySet(),
    val likedReviewIds: Set<String> = emptySet(),
    val expandedReviewIds: Set<String> = emptySet(),
    val commentedReviewIds: Set<String> = emptySet(),
    val sharedReviewIds: Set<String> = emptySet(),
    val savedPlaceIds: Set<String> = emptySet()
)
