package com.example.wayspot.ui.screens.savedplaces

import com.example.wayspot.data.model.savedplace.SavedPlace
import com.example.wayspot.data.model.savedplace.SavedPlaceList
import com.example.wayspot.data.model.savedplace.SavedPlacesRules

/** Estado derivado de la búsqueda, la lista seleccionada y los lugares guardados visibles. */
data class SavedPlacesState(
    val searchQuery: String = "",
    val selectedList: SavedPlaceList = SavedPlacesRules.defaultList,
    val destinationCount: Int = 0,
    val savedPlaces: List<SavedPlace> = emptyList(),
    val counts: Map<SavedPlaceList, Int> = emptyMap()
)
