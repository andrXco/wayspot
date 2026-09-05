package com.example.wayspot.ui.screens.explore

import com.example.wayspot.data.model.ExploreCategory
import com.example.wayspot.data.model.ExploreCategoryRules
import com.example.wayspot.data.model.Place

data class ExploreState(
    val searchText: String = "",
    val selectedCategory: ExploreCategory = ExploreCategoryRules.initialCategory,
    val categories: List<ExploreCategory> = emptyList(),
    val places: List<Place> = emptyList(),
    val savedPlaceIds: Set<String> = emptySet()
)
