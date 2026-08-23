package com.example.wayspot.data.model

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

data class ProfileNotificationPreferences(
    val newFollowers: Boolean,
    val reviewComments: Boolean,
    val likesReceived: Boolean
)

data class UserStats(
    val places: Int,
    val reviews: Int,
    val followers: String
)
