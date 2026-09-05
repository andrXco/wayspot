package com.example.wayspot.ui.screens.profile

import com.example.wayspot.data.model.Review
import com.example.wayspot.data.model.UserProfile

data class ProfileState(
    val userProfile: UserProfile? = null,
    val reviews: List<Review> = emptyList()
)
