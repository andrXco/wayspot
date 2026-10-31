package com.example.wayspot.data.model.notification

data class Notification(
    val id: String,
    val username: String,
    val type: NotificationType,
    val detail: String,
    val timestampMillis: Long,
    val avatarUrl: String? = null
)
