package com.example.wayspot.data.model

/** Perfil visible y editable de una persona usuaria dentro de la aplicación. */
data class UserProfile(
    val name: String,
    val username: String,
    val bio: String,
    val email: String,
    val location: String,
    val notificationPreferences: ProfileNotificationPreferences,
    val avatarUrl: String? = null,
    val initials: String,
    val isVerified: Boolean = false,
    val stats: UserStats
)

/** Preferencias de los tipos de notificación que muestra el perfil. */
data class ProfileNotificationPreferences(
    val newFollowers: Boolean,
    val reviewComments: Boolean,
    val likesReceived: Boolean
)

/** Métricas resumidas que acompañan al perfil. */
data class UserStats(
    val places: Int,
    val reviews: Int,
    val followers: String
)
