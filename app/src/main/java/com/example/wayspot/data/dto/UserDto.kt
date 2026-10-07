package com.example.wayspot.data.dto

import com.example.wayspot.data.model.BackendUser

data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val bio: String?,
    val location: String?,
    val avatarUrl: String?
)

fun UserDto.toBackendUser() = BackendUser(
    id = id.toString(),
    name = name,
    username = username,
    bio = bio,
    location = location,
    avatarUrl = avatarUrl
)
