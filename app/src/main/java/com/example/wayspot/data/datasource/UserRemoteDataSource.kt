package com.example.wayspot.data.datasource

import com.example.wayspot.data.dtos.RegisterUserDto

class UsernameAlreadyExistsException : IllegalStateException()

interface UserRemoteDataSource {
    suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    )

    suspend fun deleteUser(userId: String)
}
