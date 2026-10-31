
package com.example.wayspot.data.dto

import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.model.user.ProfileNotificationPreferences

data class UserFirestoreDto(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val fcmToken: String = "",
    val country: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val notificationPreferences: ProfileNotificationPreferences =
        ProfileNotificationPreferences()
) : UserDtoGeneric() {

    override fun toUserInfo(): UserInfo {
        return UserInfo(
            id = id,
            name = name,
            username = username,
            bio = bio,
            location = country,
            avatarUrl = avatarUrl,
            notificationPreferences = notificationPreferences
        )
    }
}
