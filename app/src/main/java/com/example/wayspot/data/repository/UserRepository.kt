package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.datasource.UsernameAlreadyExistsException
import com.example.wayspot.data.dtos.RegisterUserDto
import com.example.wayspot.data.model.AuthFailure
import com.example.wayspot.data.model.AuthOutcome
import com.example.wayspot.data.model.AuthRules
import com.example.wayspot.data.model.AuthSession
import com.google.firebase.FirebaseNetworkException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun registerUser(
        session: AuthSession,
        username: String
    ): AuthOutcome<Unit> = runUserOperation {
        val registerUserDto = RegisterUserDto(
            email = session.email,
            username = username.trim(),
            usernameNormalized = AuthRules.normalizedUsername(username)
        )
        remoteDataSource.registerUser(
            registerUserDto = registerUserDto,
            userId = session.uid
        )
    }

    suspend fun deleteUser(userId: String): AuthOutcome<Unit> = runUserOperation {
        remoteDataSource.deleteUser(userId)
    }

    private suspend fun runUserOperation(block: suspend () -> Unit): AuthOutcome<Unit> = try {
        block()
        AuthOutcome.Success(Unit)
    } catch (error: Exception) {
        AuthOutcome.Failure(error.toAuthFailure())
    }

    private fun Exception.toAuthFailure(): AuthFailure = when {
        this is UsernameAlreadyExistsException || cause is UsernameAlreadyExistsException -> {
            AuthFailure.UsernameAlreadyInUse
        }
        this is FirebaseNetworkException || cause is FirebaseNetworkException -> AuthFailure.Network
        else -> AuthFailure.Unknown
    }
}
