package com.example.wayspot.data.dtos

data class RegisterUserDto(
    val email: String,
    val username: String,
    val usernameNormalized: String
)
