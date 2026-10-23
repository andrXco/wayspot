
package com.example.wayspot.data.dto

import com.example.wayspot.data.model.user.ProfileNotificationPreferences

data class RegisterUserDto(
    val username: String,
    val name: String,
    val fcmToken: String,
    val country: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val notificationPreferences: ProfileNotificationPreferences =
        ProfileNotificationPreferences()
)
