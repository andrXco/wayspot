package com.example.wayspot.data.model

data class BackendUser(
    val id: String,
    val name: String,
    val username: String,
    val bio: String?,
    val location: String?,
    val avatarUrl: String?
)

object BackendSession {
    const val USER_ID = "1"
}
