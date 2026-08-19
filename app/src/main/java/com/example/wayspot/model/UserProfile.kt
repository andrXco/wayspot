package com.example.wayspot.model

data class UserProfile(
    val name: String,
    val username: String,
    val bio: String,
    val placesCount: Int,
    val reviewsCount: Int,
    val followersCount: String,
    val followingCount: Int,
    val profileImage: Any? = null
)
