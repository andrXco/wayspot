package com.example.wayspot.data.dto

import com.example.wayspot.data.model.user.ProfileNotificationPreferences

/** Campos privados de la cuenta que no se incluyen en el perfil público de Firestore. */
data class UserPrivateSettingsFirestoreDto(
    val fcmToken: String = "",
    val notificationPreferences: ProfileNotificationPreferences =
        ProfileNotificationPreferences()
)
