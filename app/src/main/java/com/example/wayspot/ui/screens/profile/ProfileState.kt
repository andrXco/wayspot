package com.example.wayspot.ui.screens.profile

import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.user.UserProfile


/** Estado inmutable que reúne el perfil visible y sus reseñas. */
data class ProfileState(
    val userProfile: UserProfile? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
