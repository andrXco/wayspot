package com.example.wayspot.ui.screens.publicprofile

import com.example.wayspot.data.model.UserInfo
import com.example.wayspot.data.model.ReviewInfo

data class PublicProfileState(
    val user: UserInfo? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
