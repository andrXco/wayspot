package com.example.wayspot.ui.screens.editprofile

data class EditProfileState(
    val username: String = "",
    val email: String = "",
    val bio: String = "",
    val location: String = "",
    val avatarUrl: String? = null,
    val newFollowersEnabled: Boolean = false,
    val reviewCommentsEnabled: Boolean = false,
    val receivedLikesEnabled: Boolean = false,
    val isDeleteConfirmationVisible: Boolean = false
)