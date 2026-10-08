package com.example.wayspot.ui.screens.publicprofile

import com.example.wayspot.data.dto.UserDto
import com.example.wayspot.data.model.ReviewInfo

data class PublicProfileState(
    val user: UserDto? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
