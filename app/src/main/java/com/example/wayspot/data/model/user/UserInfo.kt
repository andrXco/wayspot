package com.example.wayspot.data.model.user

data class UserInfo(
    val id: String,
    val name: String,
    val username: String,
    val bio: String?,
    val location: String?,
    val avatarUrl: String?
)
