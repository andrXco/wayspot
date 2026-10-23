package com.example.wayspot.ui.screens.explore

import com.example.wayspot.data.model.explore.ExploreCategory
import com.example.wayspot.data.model.explore.ExploreCategoryRules
import com.example.wayspot.data.model.place.Place

/** Estado inmutable del catálogo explorado, su filtro activo y los guardados sincronizados. */
data class ExploreState(
    val searchText: String = "",
    val selectedCategory: ExploreCategory = ExploreCategoryRules.initialCategory,
    val categories: List<ExploreCategory> = emptyList(),
    val places: List<Place> = emptyList(),
    val savedPlaceIds: Set<String> = emptySet()
)
