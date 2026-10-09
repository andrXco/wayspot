package com.example.wayspot.navigation

import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.UserProfile

/** Estado compartido entre destinos para el perfil activo y sus lugares guardados. */
data class AppNavigationState(
    val userProfile: UserProfile? = null,
    val savedPlaces: List<SavedPlace> = emptyList(),
    val commentRefreshReviewId: String? = null,
    val commentRefreshVersion: Int = 0
)
