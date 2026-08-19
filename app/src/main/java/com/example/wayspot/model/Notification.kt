package com.example.wayspot.model

data class Notification(
    val id: Int,
    val username: String,
    val message: String,
    val time: String,
    val image: Any? = null
)
