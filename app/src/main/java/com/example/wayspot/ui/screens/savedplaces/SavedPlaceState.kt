package com.example.wayspot.ui.screens.savedplaces

import com.example.wayspot.data.model.SavedPlaceList
import com.example.wayspot.data.model.SavedPlacesRules

data class SavedPlacesState(
    val searchQuery: String = "",
    val selectedList: SavedPlaceList = SavedPlacesRules.defaultList
)