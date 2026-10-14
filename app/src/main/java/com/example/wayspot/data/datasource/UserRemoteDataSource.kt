
package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.UserDtoGeneric

interface UserRemoteDataSource {

    suspend fun getUsers(): List<UserDtoGeneric>

    suspend fun getUserById(id: String): UserDtoGeneric

    suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    )
}
