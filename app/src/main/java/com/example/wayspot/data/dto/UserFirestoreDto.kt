
package com.example.wayspot.data.dto

import com.example.wayspot.data.model.user.UserInfo

data class UserFirestoreDto(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val country: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null
) : UserDtoGeneric() {

    override fun toUserInfo(): UserInfo {
        return UserInfo(
            id = id,
            name = name,
            username = username,
            bio = bio,
            location = country,
            avatarUrl = avatarUrl
        )
    }
}
