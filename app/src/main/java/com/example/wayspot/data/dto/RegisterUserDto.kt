
package com.example.wayspot.data.dto

data class RegisterUserDto(
    val username: String,
    val name: String,
    val country: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0
)
