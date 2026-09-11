package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.AuthRemoteDataSource
import com.example.wayspot.data.model.AuthFailure
import com.example.wayspot.data.model.AuthOutcome
import com.example.wayspot.data.model.AuthSession
import com.example.wayspot.data.model.AuthStatus
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class AuthRepository @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
) {
    val authStatus: Flow<AuthStatus> = remoteDataSource.observeUser()
        .map { user -> user.toStatus() }

    val currentSession: AuthSession?
        get() = remoteDataSource.currentUser?.toSession()

    suspend fun signIn(email: String, password: String): AuthOutcome<AuthSession> =
        runAuthOperation {
            remoteDataSource.signIn(email.trim(), password).toSession()
        }

    suspend fun signUp(email: String, password: String): AuthOutcome<AuthSession> =
        runAuthOperation {
            remoteDataSource.signUp(email.trim(), password).toSession()
        }

    suspend fun updateDisplayName(username: String): AuthOutcome<AuthSession> =
        runAuthOperation {
            remoteDataSource.updateDisplayName(username.trim()).toSession()
        }

    suspend fun sendPasswordReset(email: String): AuthOutcome<Unit> = runAuthOperation {
        remoteDataSource.sendPasswordReset(email.trim())
        Unit
    }

    suspend fun reauthenticate(password: String): AuthOutcome<Unit> = runAuthOperation {
        remoteDataSource.reauthenticate(password)
        Unit
    }

    suspend fun deleteCurrentUser(): AuthOutcome<Unit> = runAuthOperation {
        remoteDataSource.deleteCurrentUser()
        Unit
    }

    fun signOut() {
        remoteDataSource.signOut()
    }

    private suspend fun <T> runAuthOperation(block: suspend () -> T): AuthOutcome<T> = try {
        AuthOutcome.Success(block())
    } catch (error: Exception) {
        AuthOutcome.Failure(error.toAuthFailure())
    }

    private fun FirebaseUser?.toStatus(): AuthStatus {
        val user = this ?: return AuthStatus.SignedOut
        return AuthStatus.Authenticated(user.toSession())
    }

    private fun FirebaseUser.toSession(): AuthSession = AuthSession(
        uid = uid,
        email = email.orEmpty(),
        username = displayName,
        isEmailVerified = isEmailVerified
    )

    private fun Exception.toAuthFailure(): AuthFailure = when (this) {
        is FirebaseAuthUserCollisionException -> AuthFailure.EmailAlreadyInUse
        is FirebaseAuthInvalidCredentialsException -> AuthFailure.InvalidCredentials
        is FirebaseAuthRecentLoginRequiredException -> AuthFailure.RecentLoginRequired
        is FirebaseNetworkException -> AuthFailure.Network
        else -> AuthFailure.Unknown
    }
}
