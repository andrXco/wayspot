package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

/**
 * Contrato de autenticación para la capa de presentación.
 *
 * Delega en la fuente remota y mantiene a los ViewModels ajenos a FirebaseAuth.
 */
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    suspend fun signIn(email: String, password: String) {
        authRemoteDataSource.signIn(email, password)
    }

    suspend fun signUp(email: String, password: String) {
        authRemoteDataSource.signUp(email, password)
    }

    fun signOut() {
        authRemoteDataSource.signOut()
    }
}
