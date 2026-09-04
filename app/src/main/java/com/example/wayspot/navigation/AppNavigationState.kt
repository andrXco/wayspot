package com.example.wayspot.navigation

import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.UserProfile

data class AppNavigationState(
    val userProfile: UserProfile? = null,
    val savedPlaces: List<SavedPlace> = emptyList()
)
