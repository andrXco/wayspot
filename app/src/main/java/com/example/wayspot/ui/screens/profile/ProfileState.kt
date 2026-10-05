package com.example.wayspot.ui.screens.profile

import com.example.wayspot.data.model.ReviewInfo
import com.example.wayspot.data.model.UserProfile


/** Estado inmutable que reúne el perfil visible y sus reseñas. */
data class ProfileState(
    val userProfile: UserProfile? = null,
    val reviews: List<ReviewInfo> = emptyList()
)
