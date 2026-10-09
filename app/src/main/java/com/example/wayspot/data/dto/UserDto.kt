package com.example.wayspot.data.dto

data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val bio: String?,
    val location: String?,
    val avatarUrl: String?
)

object BackendSession {
    const val USER_ID = "1"
}
