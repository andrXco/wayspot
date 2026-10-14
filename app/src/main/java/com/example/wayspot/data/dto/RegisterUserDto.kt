package com.example.wayspot.data.dto

data class RegisterUserDto(
    val username: String,
    val name: String,
    val country: String? = null,
    val bio: String? = null
)
