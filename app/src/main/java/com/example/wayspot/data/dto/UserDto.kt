package com.example.wayspot.data.dto

import com.example.wayspot.data.model.user.UserInfo

data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val bio: String?,
    val location: String?,
    val avatarUrl: String?
)

fun UserDto.toUserInfo() = UserInfo(
    id = id.toString(),
    name = name,
    username = username,
    bio = bio,
    location = location,
    avatarUrl = avatarUrl
)

object BackendSession {
    const val USER_ID = "1"
}
