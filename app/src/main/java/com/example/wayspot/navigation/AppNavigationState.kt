package com.example.wayspot.navigation

import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.data.model.AuthStatus

data class AppNavigationState(
    val userProfile: UserProfile? = null,
    val savedPlaces: List<SavedPlace> = emptyList(),
    val authStatus: AuthStatus = AuthStatus.Loading
)
