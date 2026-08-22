package com.example.wayspot.data.model

data class UserProfile(
    val name: String,
    val username: String,
    val bio: String,
    val avatarUrl: String? = null,
    val initials: String,
    val isVerified: Boolean = false,
    val stats: UserStats
)

data class UserStats(
    val places: Int,
    val reviews: Int,
    val followers: String
)
