package com.example.wayspot.ui.screens.profile

import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.UserProfile

data class ProfileState(
    val userProfile: UserProfile = PreviewData.userProfile
)