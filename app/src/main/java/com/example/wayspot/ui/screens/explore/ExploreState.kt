package com.example.wayspot.ui.screens.explore

import com.example.wayspot.data.model.ExploreCategory
import com.example.wayspot.data.model.ExploreCategoryRules

data class ExploreState(
    val searchText: String = "",
    val selectedCategory: ExploreCategory = ExploreCategoryRules.initialCategory
)