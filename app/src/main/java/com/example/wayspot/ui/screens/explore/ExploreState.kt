package com.example.wayspot.ui.screens.explore

import com.example.wayspot.data.model.place.Place

/** Catálogo filtrado, filtros visibles y favoritos compartidos. */
data class ExploreState(
    val searchText: String = "",
    val selectedCategory: String? = null,
    val categories: List<String> = emptyList(),
    val places: List<Place> = emptyList(),
    val savedPlaceIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val loadFailed: Boolean = false
)
