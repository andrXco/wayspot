package com.example.wayspot.data.model.notification

data class Notification(
    val id: Int,
    val username: String,
    val message: String,
    val detail: String,
    val time: String
)