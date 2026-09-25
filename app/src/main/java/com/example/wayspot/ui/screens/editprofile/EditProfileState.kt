package com.example.wayspot.ui.screens.editprofile

import android.net.Uri
import com.example.wayspot.data.model.ProfileNotificationPreferences
import com.example.wayspot.data.model.UserProfile

/** Estado inmutable de la edición de perfil, incluida la validación y el diálogo destructivo. */
data class EditProfileState(
    val originalProfile: UserProfile? = null,
    val initials: String = "",
    val username: String = "",
    val email: String = "",
    val bio: String = "",
    val location: String = "",
    val avatarUrl: String? = null,
    val selectedAvatarUri: Uri? = null,
    val isUploadingAvatar: Boolean = false,
    val avatarUploadErrorMessage: String? = null,
    val uploadedAvatarUrl: String? = null,
    val notificationPreferences: ProfileNotificationPreferences =
        ProfileNotificationPreferences(
            newFollowers = false,
            reviewComments = false,
            likesReceived = false
        ),
    val isSaveEnabled: Boolean = false,
    val isDeleteConfirmationVisible: Boolean = false
)
